package com.kindustry.market

import androidx.lifecycle.ViewModel
import com.kindustry.market.db.repository.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {
    val readAll = companyRepository.readAll

}