package com.cats32.challenge

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cats32.challenge.databinding.FragmentSecondBinding
import com.cats32.challenge.ui.PointsAdapter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupMenu()
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.table.adapter = PointsAdapter()
        binding.table.layoutManager = LinearLayoutManager(requireContext())
        binding.graph.viewport.isScalable = true
        binding.graph.viewport.isScrollable = true
        collectPoints()
        collectEffects()
    }

    private fun collectPoints() {
        lifecycleScope.launchWhenResumed {
            viewModel.pointsFlow.collectLatest { points ->
                (binding.table.adapter as PointsAdapter).submit(points)
                val series = LineGraphSeries<DataPoint>()
                for (point in points) {
                    series.isDrawDataPoints = true
                    series.appendData(DataPoint(point.x, point.y), false, points.size)
                }
                binding.graph.addSeries(series)
                if (points.isNotEmpty()) {
                    binding.graph.viewport.setMinX(points[0].x)
                    binding.graph.viewport.setMaxX(points[points.lastIndex].x)
                    binding.graph.viewport.setMinY(points.minBy { it.y }.y)
                    binding.graph.viewport.setMaxY(points.maxBy { it.y }.y)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun takeSnapshot() {
        val image = binding.graph.takeSnapshot()
        if (image != null) {
            viewModel.saveImage(image)
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_snapshot -> takeSnapshot()
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun collectEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest {effect ->
                    when (effect) {
                        is MainViewModel.Effect.ShowToast -> {
                            Toast.makeText(
                                requireContext(),
                                effect.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

}