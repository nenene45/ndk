#include "Curl.h"
#include <curl/curl.h>
#include <iostream>

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

   struct curl_httppost *post = NULL;
   struct curl_httppost *last = NULL;

    if(curl) {
        curl_easy_setopt(curl, CURLOPT_URL , addr);

        //FILE *img = fopen("/sdcard/test.jpg","wb");
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0L);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 0L);
        curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);

        curl_formadd(&post, &last,
            CURLFORM_COPYNAME, "file",
            CURLFORM_FILE, "/sdcard/test.jpg",
            CURLFORM_END
        );
        curl_easy_setopt(curl, CURLOPT_HTTPPOST, post);

        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, responseWriter);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &response);
        res = curl_easy_perform(curl);
        if(res != CURLE_OK) {
            response = curl_easy_strerror(res);
        }
        curl_easy_cleanup(curl);
    }

    return env->NewStringUTF(response.c_str());
}