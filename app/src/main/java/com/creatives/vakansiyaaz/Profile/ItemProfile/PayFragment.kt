package com.creatives.vakansiyaaz.Profile.ItemProfile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.billingclient.api.*
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentPayBinding
import com.creatives.vakansiyaaz.databinding.PaymentItemListBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class PayFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPayBinding
    lateinit var billingClient: BillingClient
    private val skuList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация SKU-листа
        skuList.add("balance1")
        skuList.add("balance3")
        skuList.add("balance10")

        billingClient = BillingClient.newBuilder(requireContext())
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
        buttons()
    }

    private fun buttons() {
        binding.bAdd1.setOnClickListener { billing("balance1") }
        binding.bAdd3.setOnClickListener { billing("balance3") }
        binding.bAdd10.setOnClickListener { billing("balance10") }
    }

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            // Обработайте успешные покупки здесь
            for (purchase in purchases) {
                val purchaseDetails = purchase.skus
                if (purchaseDetails.isNotEmpty()) {

                    val sku = purchaseDetails[0]
//                    Log.d("MyLog", "Successful purchase SKU: $sku")

                    // Добавьте ваш код обработки успешных покупок
                    val paymentAmount = when (sku) {
                        "balance1" -> 1
                        "balance3" -> 5
                        "balance10" -> 10
                        else -> 0 // Установите значение по умолчанию или обработайте другие SKU
                    }
                    if (paymentAmount > 0) {
                        recordPurchaseInDatabase(sku, paymentAmount)
                        updateBalanceInDatabaseBalance(paymentAmount)

                        // Теперь информация о покупке будет записана в базу данных
                    }
                }
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Пользователь отменил покупку
            Log.d("MyLog", "Purchase canceled by user")

            showToast("Покупка отменена.")
        } else {
            // Обработка других ошибок покупок
            Log.d("MyLog", "Purchase error: ${billingResult.debugMessage}")

            showToast("Ошибка при покупке: ${billingResult.debugMessage}")
        }
    }

    private fun updateBalanceInDatabaseBalance(paymentAmount: Int) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid

        if (uid != null) {
            // Получите текущее значение balance_ads из Firebase Realtime Database
            val userRef: DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("users").child(uid)

            userRef.child("balance").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val currentBalanceAds = dataSnapshot.getValue(String::class.java)

                    // Проверка на null перед выполнением операции сложения
                    val currentBalanceInt = currentBalanceAds?.toIntOrNull() ?: 0
                    val newBalanceAds = (currentBalanceInt + paymentAmount).toString()

                    // Установите обновленное значение balance_ads
                    userRef.child("balance").setValue(newBalanceAds)
                    showToast("Баланс успешно обновлен.")
                    dismiss()

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showToast("Ошибка при обновлении баланса: ${databaseError.message}")
                }
            })
        } else {
            showToast("пользователя не найден.")
        }
    }


    private fun recordPurchaseInDatabase(sku: String, paymentAmount: Int) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid

        if (uid != null) {
            // Получите ссылку на узел пользователя в базе данных Firebase Realtime
            val userRef: DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("users").child(uid)

            // Создайте данные о транзакции
            val transactionData = HashMap<String, Any>()
            transactionData["sku"] = sku // SKU товара
            transactionData["amount"] = paymentAmount // Сумма платежа
            transactionData["timestamp"] = ServerValue.TIMESTAMP // Временная метка

            // Создайте новый узел "покупки" и установите данные транзакции в нем
            val purchasesRef = userRef.child("history_balance").push()
            purchasesRef.setValue(transactionData)
        }
    }

    private fun showToast(message: String) {
        val context = requireContext()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun billing(sku: String) {
        // Начало подключения к службе биллинга
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                // Обработка отключения от службы биллинга
                showToast("Подключение к службе отключено.")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                // Обработка завершения настройки биллинга

                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Если настройка завершена успешно

                    // Создание параметров для запроса информации о продукте
                    val params = SkuDetailsParams.newBuilder()
                        .setSkusList(listOf(sku))
                        .setType(BillingClient.SkuType.INAPP)

                    // Асинхронный запрос информации о продукте
                    billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->

                        // Обработка полученных данных о продукте
                        val skuDetails = skuDetailsList?.firstOrNull()
                        skuDetails?.let {
                            // Создание параметров для запуска процесса оплаты
                            val flowPurchase = BillingFlowParams.newBuilder()
                                .setSkuDetails(it)
                                .build()

                            // Запуск процесса оплаты
                            val responseCode =
                                billingClient.launchBillingFlow(
                                    requireActivity(),
                                    flowPurchase
                                ).responseCode

                            if (responseCode == BillingClient.BillingResponseCode.OK) {
                            } else {
                                // Обработка ошибки покупки
                                showToast("Ошибка при покупке: $responseCode")
                            }
                        }
                    }
                } else {
                    // Ошибка при настройке биллинга
                    showToast("Ошибка при настройке биллинга: ${billingResult.debugMessage}")
                }
            }
        })
    }


}