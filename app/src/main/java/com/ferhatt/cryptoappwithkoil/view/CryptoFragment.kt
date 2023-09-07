package com.ferhatt.cryptoappwithkoil.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferhatt.cryptoappwithkoil.databinding.FragmentCryptoBinding
import com.ferhatt.cryptoappwithkoil.model.CryptoModel
import com.ferhatt.cryptoappwithkoil.service.CryptoAPI
import com.ferhatt.cryptoappwithkoil.viewmodel.CryptoViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoFragment : Fragment(), RecyclerViewAdapter.Listener {

   private var _binding: FragmentCryptoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CryptoViewModel>()
    private var cryptoAdapter = RecyclerViewAdapter(arrayListOf(),this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCryptoBinding.inflate(inflater,container,false)
       val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
       // viewModel = ViewModelProvider(this).get(CryptoViewModel::class.java)
        viewModel.getDataFromAPI()
        observeLiveData()
    }

    private fun observeLiveData(){

        viewModel.cryptoList.observe(viewLifecycleOwner, Observer { crptos ->
            crptos?.let {
                binding.recyclerView.visibility = View.VISIBLE
                cryptoAdapter = RecyclerViewAdapter(ArrayList(crptos.data ?: arrayListOf()),this@CryptoFragment)
                binding.recyclerView.adapter = cryptoAdapter
            }
        })

        viewModel.cryptoError.observe(viewLifecycleOwner, Observer { error->
            error?.let {
                if(it.data == true) {
                    binding.cryptoErrorText.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.cryptoErrorText.visibility = View.GONE
                }
            }
        })

        viewModel.cryptoLoading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if (it.data == true) {
                    binding.cryptoProgressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.cryptoErrorText.visibility = View.GONE
                } else {
                    binding.cryptoProgressBar.visibility = View.GONE
                }
            }
        })

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
       Toast.makeText(requireContext(),"Clicked On : ${cryptoModel.currency}",Toast.LENGTH_SHORT).show()
    }

}