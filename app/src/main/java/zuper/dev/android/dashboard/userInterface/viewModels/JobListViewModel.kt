package zuper.dev.android.dashboard.userInterface.theme.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import zuper.dev.android.dashboard.data.DataRepository
import zuper.dev.android.dashboard.data.model.JobApiModel
import javax.inject.Inject

@HiltViewModel
class JobListViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {

    private val mutableLiveJobData = MutableLiveData<List<JobApiModel>?>(null)
    val jobsState: LiveData<List<JobApiModel>?> = mutableLiveJobData

    init {
        fetchJobs()
    }

    fun refreshJobs() {
        fetchJobs()
    }

    private fun fetchJobs() {
        viewModelScope.launch {
            mutableLiveJobData.value = dataRepository.getJobs()
        }
    }
}