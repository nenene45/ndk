#include "Curl.h"
#include <curl/curl.h>
#include <iostream>

// Curl 의 Response 처리.
size_t responseWriter(char *data, size_t length, size_t bytes, std::string *writerData) {
	if (writerData == NULL)
		return 0;
	long size = length * bytes;
	writerData->append(data,size);
	return size;
}



JNIEXPORT jstring JNICALL Java_com_android_ndk_curlActivity_getHTML(JNIEnv *env, jobject obj, jstring url) {
    CURL *curl;
    CURLcode res;
    std::string response;
    const char *addr = env->GetStringUTFChars(url, 0);
    curl = curl_easy_init();
    if(curl) {
        curl_easy_setopt(curl, CURLOPT_URL, addr);

        // 웹서버에서 모바일 접속으로 인식할 수 있도록 임의의 User Agent 값을 넣었다.
        curl_easy_setopt(curl, CURLOPT_USERAGENT,
        "Mozilla/5.0 (Linux; Android 4.4.2; Nexus 5 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.99 Mobile Safari/537.36");

        // SSL 관련 옵션.
        // 3번째 인자값이 0일 경우 인증서 진위 여부에 상관 없이 접속. 기본값 1.
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0L);
        // 3번째 인자 값이 0일 경우 인증서 이름에 상관없이 접속. 기본값 2.
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 0L);

        // redirecte 사용.
        curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);

        // post 전송시 사용.
        //curl_easy_setopt(curl, CURLOPT_POSTFIELDS, "name=daniel&project=curl");

        // response 의 response 를 처리하는 함수 설정.
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, responseWriter);
        // 세 번째 인자에는 responseWriter 함수로 처리될 response 객체의 레퍼런스가 들어간다.
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &response);

        res = curl_easy_perform(curl);
        // 실패할 경우 메세지 출력.
        if(res != CURLE_OK) {
            response = curl_easy_strerror(res);
        }
        curl_easy_cleanup(curl);
    }

    return env->NewStringUTF(response.c_str());
}