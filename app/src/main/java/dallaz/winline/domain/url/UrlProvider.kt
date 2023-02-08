package dallaz.winline.domain.url

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dallaz.winline.app.di.AppScope
import dallaz.winline.app.prefs.AppPrefs
import dallaz.winline.domain.connectivity.ConnectionStatus
import dallaz.winline.domain.connectivity.ConnectivityProvider
import dallaz.winline.domain.exceptions.FRCErrorOccurredException
import dallaz.winline.domain.exceptions.FRCTaskFailedException
import dallaz.winline.domain.exceptions.NoInternetConnectionException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@AppScope
class UrlProvider @Inject constructor(
    appPrefs: AppPrefs,
    connectivityProvider: ConnectivityProvider,
) {
    private val remoteConfig = Firebase.remoteConfig

    /**
     * Provides flow with single url or throws exception
     *
     * 1. Checks internet connection
     * 2. Gets saved url
     * 3. Gets remote url if saved is empty and saves it
     *
     * @throws NoInternetConnectionException
     * @throws FRCErrorOccurredException
     * @throws FRCTaskFailedException
     */
    val urlFlow = connectivityProvider.status.flatMapLatest {
        if (it is ConnectionStatus.Disconnected) throw NoInternetConnectionException()
        else appPrefs.urlFlow.map { savedUrl ->
            savedUrl.ifEmpty {
                var remoteUrl = savedUrl
                try {
                    remoteUrl = getFRCStringSuspend(FRC_URL_PARAM_NAME)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                remoteUrl
            }
        }
    }.onEach {
        if (appPrefs.url != it) appPrefs.url = it
    }.flowOn(Dispatchers.IO)


    /**
     * Gets Firebase Remote Config string value
     *
     * @param name remote parameter name
     * @throws FRCErrorOccurredException
     * @throws FRCTaskFailedException
     */
    private suspend fun getFRCStringSuspend(name: String) =
        suspendCoroutine { continuation ->
            remoteConfig.fetch(0).addOnCompleteListener {
                remoteConfig.activate().addOnCompleteListener {
                    continuation.resume(remoteConfig.getString(name))
                }.addOnFailureListener {
                    continuation.resumeWithException(FRCTaskFailedException())
                }
            }.addOnFailureListener {
                continuation.resumeWithException(FRCErrorOccurredException())
            }
        }

    companion object {
        private const val FRC_URL_PARAM_NAME = "url"
    }
}