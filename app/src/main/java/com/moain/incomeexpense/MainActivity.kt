package com.moain.incomeexpense

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.moain.incomeexpense.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    private var totalIncome = 0.0
    private var totalExpense = 0.0
    private val transactions = mutableListOf<Transaction>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()
        updateUI()
    }
    
    private fun setupClickListeners() {
        binding.btnAddIncome.setOnClickListener {
            addTransaction(isIncome = true)
        }
        
        binding.btnAddExpense.setOnClickListener {
            addTransaction(isIncome = false)
        }
        
        binding.btnClear.setOnClickListener {
            clearAll()
        }
    }
    
    private fun addTransaction(isIncome: Boolean) {
        val amountText = binding.etAmount.text.toString()
        val description = binding.etDescription.text.toString()
        
        if (amountText.isEmpty()) {
            Toast.makeText(this, "الرجاء إدخال المبلغ", Toast.LENGTH_SHORT).show()
            return
        }
        
        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "الرجاء إدخال مبلغ صحيح", Toast.LENGTH_SHORT).show()
            return
        }
        
        val transaction = Transaction(
            amount = amount,
            description = description.ifEmpty { if (isIncome) "دخل" else "مصروف" },
            isIncome = isIncome
        )
        
        transactions.add(transaction)
        
        if (isIncome) {
            totalIncome += amount
            Toast.makeText(this, "تم إضافة دخل: ${formatCurrency(amount)}", Toast.LENGTH_SHORT).show()
        } else {
            totalExpense += amount
            Toast.makeText(this, "تم إضافة مصروف: ${formatCurrency(amount)}", Toast.LENGTH_SHORT).show()
        }
        
        // Clear inputs
        binding.etAmount.text?.clear()
        binding.etDescription.text?.clear()
        
        updateUI()
    }
    
    private fun clearAll() {
        totalIncome = 0.0
        totalExpense = 0.0
        transactions.clear()
        updateUI()
        Toast.makeText(this, "تم مسح جميع البيانات", Toast.LENGTH_SHORT).show()
    }
    
    private fun updateUI() {
        val balance = totalIncome - totalExpense
        
        binding.tvTotalIncome.text = formatCurrency(totalIncome)
        binding.tvTotalExpense.text = formatCurrency(totalExpense)
        binding.tvBalance.text = formatCurrency(balance)
        
        // Change balance color based on value
        val balanceColor = when {
            balance > 0 -> getColor(R.color.income_green)
            balance < 0 -> getColor(R.color.expense_red)
            else -> getColor(R.color.neutral_gray)
        }
        binding.tvBalance.setTextColor(balanceColor)
        
        // Update transactions list
        val transactionsText = if (transactions.isEmpty()) {
            "لا توجد معاملات"
        } else {
            transactions.reversed().joinToString("\n") { t ->
                val sign = if (t.isIncome) "+" else "-"
                val color = if (t.isIncome) "🟢" else "🔴"
                "$color ${t.description}: $sign${formatCurrency(t.amount)}"
            }
        }
        binding.tvTransactions.text = transactionsText
    }
    
    private fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("ar", "SA"))
        return format.format(amount)
    }
    
    data class Transaction(
        val amount: Double,
        val description: String,
        val isIncome: Boolean
    )
}
