package com.example.kotlincaculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlincaculator.databinding.ActivityMainBinding
//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var canAddOperation = false
    private var canAddDecimal = true
    private var canAddNumber = true


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }


        binding.workingTV.text = ""
        binding.resultTV.text = ""
    }

    fun numberAction(view: View)
    {
        if(view is Button){
            if(view.text == "."){
                if(canAddDecimal)
                    binding.workingTV.append(view.text)
                canAddDecimal = false
            }
            else binding.workingTV.append(view.text)
            canAddOperation = true
        }
    }
    fun operationAction(view: View)
    {
        if(view is Button && canAddOperation){
            binding.workingTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
    fun allClearAction(view: View)
    {
        binding.workingTV.text = ""
        binding.resultTV.text = ""
    }
    fun backSpaceAction(view: View)
    {
        val length = binding.workingTV.text.length
        if (length > 0)
            binding.workingTV.text = binding.workingTV.text.subSequence(0, length - 1)
    }
    fun equalAction(view: View)
    {
        binding.resultTV.text = calculateResults()
    }

    private fun calculateResults(): String
    {
        val digitsOperator = digitsOperators()

        if(digitsOperator.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperator)

        if(timesDivision.isEmpty()) return ""

        val result = addSubCalculator(timesDivision)

        return result.toString()
    }

    private fun addSubCalculator(list: MutableList<Any>): Float
    {
        var result = list[0] as Float

        for(i in list.indices){
            if(list[i] is Char && i!=list.lastIndex){
                val operator = list[i]
                val nextDigit = list[i+1] as Float
                if(operator == '+')
                    result += nextDigit
                if(operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList

        while(list.contains('x')||list.contains('/')){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(list: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = list.size

        for(i in list.indices){
            if(list[i] is Char && i!=list.lastIndex && i<restartIndex){

                val operator = list[i]
                val prevDigit = list[i-1] as Float
                val nextDigit = list[i+1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i+1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i+1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i>restartIndex)
                newList.add(list[i])
        }
        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()

        var currentDigit = ""

        for(char in binding.workingTV.text){
            if(char.isDigit() || char == '.')
                currentDigit += char
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(char)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())



        return list
    }
}











