package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var expression: Expression
    var lastNumeric = false
    var stateError = false
    var lastDot = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val text = binding.tvData.text.toString()

            expression = ExpressionBuilder(text).build()

            try {
                val res = expression.evaluate()
                binding.tvResult.visibility = View.VISIBLE
                binding.tvResult.text = "=" + res
            } catch (ex: Exception) {
                Log.e("evaluate error: ", ex.toString())
                binding.tvResult.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

    fun onDigitClick(view: View) {
        if (stateError) {
            binding.tvData.text = (view as Button).text
            stateError = false
        } else {
            binding.tvData.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }

    fun onOperatorClick(view: View) {
        if (!stateError && lastNumeric) {
            binding.tvData.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.tvResult.text = binding.tvResult.text.toString().drop(1)
    }

    fun onDeleteClick(view: View) {
        binding.tvResult.text = binding.tvResult.text.toString().dropLast(1)

        try {
            val lastChar = binding.tvData.text.toString().last()
            if (lastChar.isDigit()) {
                onEqual()
            }

        } catch (e: Exception) {
            binding.tvResult.text = ""
            binding.tvResult.visibility = View.GONE
            Log.e("Last char Error",e.toString() )
        }

    }

    fun onClearClick(view: View) {
        binding.tvData.text = ""
        lastNumeric = false
    }
    fun onAllClearClick(view: View) {
        binding.tvData.text = ""
        binding.tvResult.text = ""
        stateError = false
        lastNumeric = false
        lastDot = false
        binding.tvResult.visibility = View.GONE
    }

}