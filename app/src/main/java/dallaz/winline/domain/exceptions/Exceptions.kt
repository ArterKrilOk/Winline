package dallaz.winline.domain.exceptions

open class FRCException(message: String) : Exception(message)
class FRCTaskFailedException : FRCException("FRC task failed")
class FRCErrorOccurredException : FRCException("FRC error")

class NoInternetConnectionException : Exception("No Internet connection")

class DeviceConfigurationException : Exception("Wrong device configuration")

open class WorkoutException(message: String) : Exception(message)
class NoWorkoutException : WorkoutException("No active workouts")