package com.creatives.vakansiyaaz.ADD.Spinners

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpheraAdapter
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpinnerViewModel
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentCityBinding
import com.creatives.vakansiyaaz.databinding.FragmentSferaWorkBinding

class CityFragment : Fragment() {
    private lateinit var binding: FragmentCityBinding
    private lateinit var viewModel: SpinnerViewModel
    private lateinit var adapter: SpheraAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SpinnerViewModel::class.java]
        initRc()
        search()

    }
    private fun search() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filterList(newText.orEmpty())
                return true
            }
        })
    }

    private fun initRc() {
        adapter = SpheraAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter
        adapter.setData(citiesInAzerbaijan )
        adapter.onItemClick = { adminData ->
            viewModel.city.value = adminData
           fragmentManager!!.popBackStack()
        }

    }
    private val citiesInAzerbaijan = listOf(
            "Агдам - Ağdam",
            "Агдаш - Ağdaş",
            "Агдере - Ağdərə",
            "Агджабеди - Ağcabədi",
            "Агстафа - Ağstafa",
            "Аджикабул - Hacıqabul",
            "Астара - Astara",
            "Ахсу - Ağsu",
            "Бабек - Babək",
            "Баку - Bakı",
            "Балакен - Balakən",
            "Барда - Bərdə",
            "Бейлаган - Beyləqan",
            "Билясувар - Biləsuvar",
            "Габала - Qəbələ",
            "Газах - Qazax",
            "Гах - Qax",
            "Геранбой - Goranboy",
            "Гёйгёль - Göygöl",
            "Гёйтепе - Göytəpə",
            "Гёйчай - Göyçay",
            "Гобустан - Qobustan",
            "Говлар - Qovlar",
            "Горадиз - Horadiz",
            "Губа - Quba",
            "Губадлы - Qubadlı",
            "Гусар - Qusar",
            "Гянджа - Gəncə",
            "Далимамедли - Dəliməmmədli",
            "Дашкесан - Daşkəsən",
            "Джалилабад - Cəlilabad",
            "Джебраил - Cəbrayıl",
            "Джульфа - Culfa",
            "Евлах - Yevlax",
            "Загатала - Zaqatala",
            "Зангелан - Zəngilan",
            "Зердаб - Zərdab",
            "Имишли - İmişli",
            "Исмаиллы - İsmayıllı",
            "Кедабек - Gədəbəy",
            "Кельбаджар - Kəlbəcər",
            "Кюрдамир - Kürdəmir",
            "Лачын - Laçın",
            "Ленкорань - Lənkəran",
            "Лерик - Lerik",
            "Лиман - Liman",
            "Масаллы - Masallı",
            "Мингечевир - Mingəçevir",
            "Нафталан - Naftalan",
            "Нахчыван - Naxçıvan",
            "Нефтечала - Neftçala",
            "Огуз - Oğuz",
            "Ордубад - Ordubad",
            "Саатлы - Saatlı",
            "Сабирабад - Sabirabad",
            "Сальян - Salyan",
            "Самух - Samux",
            "Сиазань - Siyəzən",
            "Сумгайыт - Sumqayıt",
            "Тертер - Tərtər",
            "Товуз - Tovuz",
            "Уджар - Ucar",
            "Физули - Füzuli",
            "Ханкенди - Xankəndi",
            "Хачмаз - Xaçmaz",
            "Ходжавенд - Xocavənd",
            "Ходжалы - Xocalı",
            "Худат - Xudat",
            "Хызы - Xızı",
            "Хырдалан - Xırdalan",
            "Шабран - Şabran",
            "Шамкир - Şəmkir",
            "Шарур - Şərur",
            "Шахбуз - Şahbuz",
            "Шеки - Şəki",
            "Шемахы - Şamaxı",
            "Ширван - Şirvan",
            "Шуша - Şuşa",
            "Ярдымлы"
        )


}