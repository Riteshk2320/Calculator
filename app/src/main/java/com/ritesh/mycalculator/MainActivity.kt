package com.ritesh.mycalculator

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    var lastNumeric = false
    var stateError = false
    var lastDot = false
    var expressionTV_visible = true
    var equalclick = false
    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun digitclick(view: View) {
        if (stateError) {
            expression_textview.text = (view as Button).text
            stateError = false
        } else {
            expression_textview.append((view as Button).text)
        }
        lastNumeric = true
        equal()
    }

    fun clearclick(view: View) {
        if (expressionTV_visible) {
            expression_textview.text = ""
            lastNumeric = false
        } else {
            expression_textview.visibility = View.VISIBLE
            expression_textview.text = ""
        }
    }

    fun backspaceclick(view: View) {
        expression_textview.text = expression_textview.text.toString().dropLast(1)
        try {
            var lastCharacter = expression_textview.text.last()
            if (lastCharacter.isDigit()) {
                equal()
            }
        } catch (ex: Exception) {
            result_textview.text = ""
            result_textview.visibility = View.GONE
            Log.e("last char error", ex.toString())
        }
    }

    fun operatorclick(view: View) {
        if (!stateError && lastNumeric && !equalclick) {
            expression_textview.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            equal()
        }
        else if (equalclick){
            expression_textview.visibility = View.VISIBLE
            expression_textview.append((view as Button).text).toString()
        }
    }

    fun equalToclick(view: View) {
        equal()
        //expression_textview.text = result_textview.toString().drop(1)
        expression_textview.text = result_textview.text.toString()
        expression_textview.visibility = View.GONE
        expressionTV_visible = false
        equalclick = true
        result_textview.setTextColor(Color.parseColor("#000000"))
    }

    fun allClearclick(view: View) {
        expression_textview.text = ""
        result_textview.text = ""
        lastNumeric = false
        lastDot = false
        stateError = false
        result_textview.visibility = View.GONE
    }

    private fun equal() {
        if (lastNumeric && !stateError) {
            var data_enter: String = expression_textview.text.toString()
            expression = ExpressionBuilder(data_enter).build()
            try {
                var result = expression.evaluate()
                result_textview.visibility = View.VISIBLE
                result_textview.text = result.toString()
            } catch (ex: ArithmeticException) {
                Log.e("Error", ex.toString())
                result_textview.visibility = View.VISIBLE
                result_textview.text = "Error"
                stateError = true
                lastNumeric = false
            }

        }
    }
}