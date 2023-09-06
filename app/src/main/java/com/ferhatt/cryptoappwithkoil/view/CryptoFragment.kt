package com.ferhatt.cryptoappwithkoil.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferhatt.cryptoappwithkoil.databinding.FragmentCryptoBinding
import com.ferhatt.cryptoappwithkoil.model.CryptoModel
import com.ferhatt.cryptoappwithkoil.service.CryptoAPI
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoFragment : Fragment(), RecyclerViewAdapter.Listener {

   // private var binding : FragmentCryptoBinding ?= null
   private var _binding: FragmentCryptoBinding? = null
    private val binding get() = _binding!!

    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels : ArrayList<CryptoModel> ?= null
    private var recyclerViewAdapter : RecyclerViewAdapter ?= null
    private var job : Job ?= null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error : ${throwable.localizedMessage}")
    }

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

        loadData()
    }

    private fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = retrofit.getData()
            println("get data called")
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    println("Succesful")
                    response.body()?.let {
                        cryptoModels = ArrayList(it)
                        cryptoModels?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it,this@CryptoFragment)
                            binding.recyclerView.adapter= recyclerViewAdapter
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job?.cancel()
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
       Toast.makeText(requireContext(),"Clicked On : ${cryptoModel.currency}",Toast.LENGTH_SHORT).show()
    }

}