package zuper.dev.android.dashboard.userInterface.theme.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import zuper.dev.android.dashboard.data.DataRepository
import zuper.dev.android.dashboard.data.model.InvoiceApiModel
import zuper.dev.android.dashboard.data.model.JobApiModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {

    private val mutableLiveJobData = MutableLiveData<List<JobApiModel>?>(null)
    val jobsState: LiveData<List<JobApiModel>?> = mutableLiveJobData

    private val mutableLiveInvoiceData = MutableLiveData<List<InvoiceApiModel>?>(null)
    val invoicesState: LiveData<List<InvoiceApiModel>?> = mutableLiveInvoiceData

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            dataRepository.observeJobs().collect { jobsList ->
                mutableLiveJobData.value = jobsList
            }
        }
        viewModelScope.launch {
            dataRepository.observeInvoices().collect { invoicesList ->
                mutableLiveInvoiceData.value = invoicesList
            }
        }
    }
}