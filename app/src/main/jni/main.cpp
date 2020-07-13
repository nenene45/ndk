#include "Curl.h"
#include <curl/curl.h>
#include <sys/stat.h>
#include <iostream>
#include <android/log.h>


size_t responseWriter(char *data, size_t length, size_t bytes, std::string *writerData) {
    __android_log_print(ANDROID_LOG_DEBUG, "curl", "Message3 : %s", writerData);
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

    struct stat file_info;
    FILE *fd;

    const char *addr = env->GetStringUTFChars(url, 0);
    curl_global_init(CURL_GLOBAL_ALL);
    curl = curl_easy_init();

    __android_log_print(ANDROID_LOG_DEBUG, "curl", "Message1");
    fd = fopen("/sdcard/test.jpg","rb");
    if (!fd){
        response = "1";
    }else if (fstat (fileno (fd), & file_info) != 0){
        response = "2";
    }else if (curl) {

          __android_log_print(ANDROID_LOG_DEBUG, "curl", "Message2");

        curl_easy_setopt(curl, CURLOPT_URL , addr);
        curl_easy_setopt(curl, CURLOPT_UPLOAD , 1L);
        curl_easy_setopt(curl, CURLOPT_READDATA , fd);

        curl_easy_setopt (curl, CURLOPT_INFILESIZE_LARGE ,(curl_off_t) file_info.st_size);

        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, responseWriter);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &response);
        res = curl_easy_perform(curl);
        if(res != CURLE_OK) {
            response = curl_easy_strerror(res);
        }

        curl_easy_cleanup(curl);
        fclose (fd);
        curl_global_cleanup();
    }
    return env->NewStringUTF(response.c_str());
}