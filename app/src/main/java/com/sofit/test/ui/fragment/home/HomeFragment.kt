package com.sofit.test.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sofit.test.BR
import com.sofit.test.data.model.Team
import com.sofit.test.databinding.FragmentHomeBinding
import com.sofit.test.ui.base.BaseFragment
import com.sofit.test.utils.GridSpacingItemDecoration
import com.sofit.dadday.util.RecyclerItemClickListener
import com.sofit.test.ui.fragment.home.chatList.ChatListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.sofit.test.R
import com.sofit.test.ui.fragment.home.friendList.FriendListFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),HomeNavigator{

    @Inject
    lateinit var homeViewModel: HomeViewModel

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getViewModel(): HomeViewModel {
        return homeViewModel
    }

    override fun getLifeCycleOwner(): LifecycleOwner = this


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding = getViewDataBinding()
        homeViewModel.setNavigator(this)
        setUp()
    }


    private fun setUp() {
        val viewpagger: ViewPager2 =home_fragment_viewPager
        viewpagger.isSaveEnabled=false
        val tabs: TabLayout = home_fragment_tabs
        val adapter =MyViewPagerAdapter(childFragmentManager,lifecycle)
        adapter.addFragment(ChatListFragment(), getString(R.string.chats))
        adapter.addFragment(FriendListFragment(), getString(R.string.friends))
        adapter.notifyDataSetChanged()
        viewpagger.adapter =adapter
        TabLayoutMediator(tabs, viewpagger) { tab, position ->
            tab.text = adapter.getPageTitle(position)
            viewpagger.setCurrentItem(tab.position, true)
        }.attach()

    }

    class MyViewPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) :  FragmentStateAdapter(manager,lifecycle ){
        private val fragmentList : MutableList<Fragment> =ArrayList()
        private val titleList : MutableList<String> =ArrayList()
        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return  fragmentList[position]
        }


        fun addFragment(fragment: Fragment, title: String){
            fragmentList.add(fragment)
            titleList.add(title)
        }

        fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
    }
}
