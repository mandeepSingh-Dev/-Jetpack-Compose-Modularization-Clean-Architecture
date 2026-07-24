/*
package ai.os.core_common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import java.io.File

class AmazonS3Uploader {

    enum class AwsKeys(val keyPath : String){
        PROFILE_IMAGE(keyPath = "ZODIAQ/profile_Image/"),
    }

    data class UploadHandler(
        val transferObserver: TransferObserver?,
        val transferUtility: TransferUtility?
    )

    companion object {

        @SuppressLint("SuspiciousIndentation")
        fun upload(
            context: Context,
            awsCredentialsData: AwsCredentialsData?,
            fileName: String,
            file: File,
            key : String,
            callbacks: (state: TransferState?, url: String?) -> Unit,
            onnProgressChanged: (currentBytes: Long, totalBytes: Long) -> Unit,
        ) : UploadHandler?{

            try {
                Log.d("Fbmfkbmfk", file.toString())
                Log.d("Fbmfkbmfk", fileName.toString())

                val s3Client = awsCredentialsData?.let { getS3Client(it) }

                val transferUtility = TransferUtility.builder()
                    .context(context)
                    .s3Client(s3Client)
                    .defaultBucket(awsCredentialsData?.AWS_BUCKET_NAME)
                    .build()

                val key = "$key$fileName"

                val observer: TransferObserver = transferUtility.upload(awsCredentialsData?.AWS_BUCKET_NAME, key, file)

                observer.setTransferListener(object : TransferListener {
                    override fun onStateChanged(id: Int, state: TransferState?) {

                        Log.d("dcmdkcndkncd", state.toString())

                        when (state) {

                            TransferState.COMPLETED -> {
                                val result =
                                    s3Client?.getUrl(awsCredentialsData.AWS_BUCKET_NAME, key)
                                callbacks(TransferState.COMPLETED, result.toString())
                            }
                            else -> {
                                callbacks(state, null)
                            }
                        }
                    }
                    override fun onProgressChanged(
                        id: Int,
                        bytesCurrent: Long,
                        bytesTotal: Long
                    ) {
                        Log.d("dklmdkcdk", bytesCurrent.toString())
                        onnProgressChanged(bytesCurrent, bytesTotal)
                    }

                    override fun onError(id: Int, ex: Exception?) {
                        Log.d("mkkdmckdcmnkd", ex?.message.toString())
                        callbacks(TransferState.FAILED,ex?.message)
                    }
                })

                return UploadHandler(transferObserver = observer, transferUtility = transferUtility)
            } catch (e: Exception) {
                callbacks(TransferState.FAILED,e.message)
                Log.d("dvmkdvnmd", e.message.toString())
                return null
            }

        }

        private fun getS3Client(awsCredentialsData: AwsCredentialsData): AmazonS3Client {

            Log.d("fblmfkbmvfk", awsCredentialsData.toString())

            val clientConfiguration = getClientConfiguration()

            val s3Client = AmazonS3Client(object : AWSCredentials {
                override fun getAWSAccessKeyId(): String {
                    return awsCredentialsData.AWS_ACCESS_KEY_ID ?: ""
                }

                override fun getAWSSecretKey(): String {
                    return awsCredentialsData.AWS_SECRET_ACCESS_KEY ?: ""

                }
            }, Region.getRegion(awsCredentialsData.AWS_REGION), clientConfiguration)

            return s3Client

        }

        private fun getClientConfiguration(): ClientConfiguration {
            val clientConfiguration = ClientConfiguration()
            clientConfiguration.maxErrorRetry = 10
            clientConfiguration.connectionTimeout = 5 * 60 * 1000
            clientConfiguration.socketTimeout = 5 * 60 * 1000

            return clientConfiguration
        }
    }


}




*/
