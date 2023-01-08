package com.pisakov.skillproj.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pisakov.skillproj.R
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.data.entity.Notification
import com.pisakov.skillproj.databinding.FragmentLaterBinding
import com.pisakov.skillproj.notifications.NotificationHelper
import com.pisakov.skillproj.utils.AnimationHelper
import com.pisakov.skillproj.utils.AutoDisposable
import com.pisakov.skillproj.utils.TopSpacingItemDecoration
import com.pisakov.skillproj.utils.addTo
import com.pisakov.skillproj.view.rv_adapters.NotificationListRecyclerAdapter
import com.pisakov.skillproj.viewmodel.LaterFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class LaterFragment : Fragment() {
    private lateinit var binding: FragmentLaterBinding
    private lateinit var notificationAdapter: NotificationListRecyclerAdapter
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(LaterFragmentViewModel::class.java)
    }
    private val autoDisposable = AutoDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_later, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLaterBinding.bind(view)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.laterFragmentRoot, requireActivity(), 5)
        autoDisposable.bindTo(lifecycle)
        initRV()
    }

    private fun initRV() {
        binding.mainRecycler.apply {
            notificationAdapter = NotificationListRecyclerAdapter(
                edit = { item: Pair<Notification, Film> ->
                    NotificationHelper.notificationSet(requireContext(), item.second)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                           viewModel.editNotification(it)
                        }.addTo(autoDisposable)
                     },
                delete = { film: Film ->
                    viewModel.deleteNotification(film.id)
                    NotificationHelper.cancelWatchLaterEvent(requireContext(), film)
                },
                click = { film: Film ->
                    view?.findNavController()
                        ?.navigate(LaterFragmentDirections.actionLaterFragmentToDetailsFragment(film))
                }
            )
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(TopSpacingItemDecoration(8))
        }
        viewModel.getNotificationList()
            .subscribeOn(Schedulers.io())
            .flatMap {
                val idList: List<Int> = it.map { notification -> notification.id }
                Observable.zip(Observable.just(it), viewModel.getFilmsFromId(idList)) { a, b ->
                    a.zip(b)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                notificationAdapter.submitList(it)
            }.addTo(autoDisposable)
    }
}