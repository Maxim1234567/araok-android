package ru.araok.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response
import ru.araok.App
import ru.araok.backgroundThreadShortToast
import ru.araok.data.dto.JwtResponseDto
import ru.araok.data.dto.RefreshJwtRequestDto

private const val FORBIDDEN_CODE = 403
private const val UNAUTHORIZED_CODE = 401

class AuthorizationResponseInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        Log.d("AuthorizationResponseInterceptor", "intercept")

        if(response.code() == FORBIDDEN_CODE) {
//            backgroundThreadShortToast(App.getContext(), "Доступ к запрошенному ресурсу запрещен")
            runBlocking {
                withContext(Dispatchers.IO) {
                    val refreshToken = RefreshJwtRequestDto(
                        Repository.getRefreshToken(App.getContext())
                    )

                    var jwtResponse = RetrofitService.araokApi.accessToken(refreshToken).body() ?: JwtResponseDto()

                    jwtResponse = RetrofitService.araokApi
                        .refreshToken("Bearer " + jwtResponse.accessToken, refreshToken).body() ?: JwtResponseDto()

                    Log.d("AuthorizationResponseInterceptor", "refresh token complete")

                    Repository.saveAccessToken(context = App.getContext(), accessToken = jwtResponse.accessToken ?: "")
                    Repository.saveRefreshToken(context = App.getContext(), refreshToken = jwtResponse.refreshToken ?: "")
                }
            }
        } else if(response.code() == UNAUTHORIZED_CODE) {
            backgroundThreadShortToast(App.getContext(), "Клиент не авторизован")
        }

        return response
    }
}