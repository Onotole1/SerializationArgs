package com.example.serializationargs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.serializationargs.utils.args.FragmentArgs
import com.example.serializationargs.databinding.FragmentMainBinding

class TestFragment : Fragment(R.layout.fragment_main) {

    companion object {
        var Bundle.arg: FeedArguments? by FragmentArgs.parcelable(FeedArguments.serializer())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val arg = arguments?.arg

        binding.content.setText(arg?.content)

        return binding.root
    }
}
