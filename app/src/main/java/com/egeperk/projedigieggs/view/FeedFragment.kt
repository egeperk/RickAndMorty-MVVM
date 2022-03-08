package com.egeperk.projedigieggs.view

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.egeperk.projedigieggs.*
import com.egeperk.projedigieggs.adapter.ItemAdapter
import com.egeperk.projedigieggs.databinding.DialogOptionsBinding
import com.egeperk.projedigieggs.databinding.FragmentMainBinding
import com.egeperk.projedigieggs.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FeedFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val charAdapter by lazy { ItemAdapter() }
    private val viewModel by viewModels<FeedViewModel>()


    private var dialogBinding: DialogOptionsBinding? = null

    /* private var _binding: FragmentMainBinding? = null
     private val binding get() = _binding!!*/
    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private val TAG = "MainFragment"
    private var characters = mutableListOf<CharactersQuery.Result>()

    //private val adapter = CharAdapter(characters)
    private val channel = Channel<Unit>(Channel.CONFLATED)
    private var lastAppliedQuery = ""
    private var page: Int? = 0
    var currentQuery = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = charAdapter
        viewModel.queryCharList()


        /*
               binding.recyclerView.adapter = adapter*/


        //viewModel.channel.trySend(Unit)
        charAdapter.onEndOfListReached = {
            viewModel.queryCharList()
            //viewModel.channel.trySend(Unit)
        }

        super.onViewCreated(view, savedInstanceState)

        //getCharacters()

        observeLiveData()

        binding.filterBtn.setOnClickListener(View.OnClickListener {
            createPopup()
        })
    }


    private fun observeLiveData() {
        viewModel.charactersList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.ViewState.Loading -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is State.ViewState.Success -> {
                    if (response.value?.data?.characters?.results?.size == 0) {
                        charAdapter.submitList(emptyList())
                        binding.recyclerView.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE

                    } else {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                    val results = response.value?.data?.characters?.results
                    //val list = charAdapter.currentList + results
                    results?.let { charAdapter.submitList(charAdapter.currentList.plus(it)) }
                    //charAdapter.submitList(mutableListOf(list))
                    binding.progressBar.visibility = View.GONE

                }
                is State.ViewState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    charAdapter.submitList(emptyList())
                    binding.recyclerView.visibility = View.GONE
                }
            }
        }
    }


/*    private fun getCharacters() {

        lifecycleScope.launchWhenResumed {

            for (item in channel) {

                val response = try {

                    apolloClient.query(
                        CharactersQuery(
                            Optional.Present(page), Optional.Present(currentQuery)
                        )
                    ).execute()

                } catch (e: ApolloException) {
                    Log.d(TAG, "onViewCreated: Error ", e)
                    return@launchWhenResumed
                }


                val newCharacters = response.data?.characters?.results?.filterNotNull()

                if (currentQuery != lastAppliedQuery) {
                    characters.clear()

                }

                if (newCharacters != null) {
                    characters.addAll(newCharacters)
                }


                lastAppliedQuery = currentQuery

                adapter.notifyDataSetChanged()


                page = response.data?.characters?.info?.next

            }

            adapter.onEndOfListReached = null
            channel.close()
        }
    }*/

    private fun createPopup() {

        dialogBuilder = AlertDialog.Builder(context)
        val layoutInflater = LayoutInflater.from(context)
        dialogBinding = DialogOptionsBinding.inflate(layoutInflater)
        dialogBuilder.setView(dialogBinding!!.root)
        dialog = dialogBuilder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()


        dialogBinding?.btnOption1?.setOnClickListener {
            dialogBinding?.btnOption2?.setImageResource(R.drawable.ellipse1)
            dialogBinding?.btnOption3?.setImageResource(R.drawable.ellipse1)
            dialogBinding?.btnOption1?.setImageResource(R.drawable.groupellipse1)

            currentQuery = "Rick"
            if (currentQuery != lastAppliedQuery) {
                page = 0
            }
            channel.trySend(Unit)

        }
        dialogBinding?.btnOption2?.setOnClickListener {
            dialogBinding?.btnOption1?.setImageResource(R.drawable.ellipse1)
            dialogBinding?.btnOption3?.setImageResource(R.drawable.ellipse1)
            dialogBinding?.btnOption2?.setImageResource(R.drawable.groupellipse1)

            currentQuery = "Morty"
            if (currentQuery != lastAppliedQuery) {
                page = 0
            }
            channel.trySend(Unit)

        }

        dialogBinding?.btnOption3?.setOnClickListener {
            dialogBinding?.btnOption1?.setImageResource(R.drawable.ellipse1)
            dialogBinding?.btnOption2?.setImageResource(R.drawable.ellipse1)
            dialogBinding?.btnOption3?.setImageResource(R.drawable.groupellipse1)


            currentQuery = ""
            if (currentQuery != lastAppliedQuery) {
                page = 0
            }
            channel.trySend(Unit)

        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}