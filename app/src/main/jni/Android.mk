LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := jniCalculator
LOCAL_SRC_FILES := Calculator.cpp
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)


# --------------- crypto ---------------
include $(CLEAR_VARS)
LOCAL_MODULE := crypto
LOCAL_MODULE_FILENAME := crypto
LOCAL_SRC_FILES := curl/$(TARGET_ARCH_ABI)/libcrypto.a
LOCAL_EXPORT_LDLIBS := -lz
include $(PREBUILT_STATIC_LIBRARY)

# --------------- ssl ---------------
LOCAL_MODULE := ssl
LOCAL_MODULE_FILENAME := ssl
LOCAL_SRC_FILES := curl/$(TARGET_ARCH_ABI)/libssl.a
include $(PREBUILT_STATIC_LIBRARY)

# --------------- libcurl ---------------
include $(CLEAR_VARS)
LOCAL_MODULE := curl
LOCAL_MODULE_FILENAME := curl
LOCAL_SRC_FILES := curl/$(TARGET_ARCH_ABI)/libcurl.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/curl/include
LOCAL_STATIC_LIBRARIES += ssl
LOCAL_STATIC_LIBRARIES += crypto
include $(PREBUILT_STATIC_LIBRARY)

# --------------- http ---------------

include $(CLEAR_VARS)
LOCAL_MODULE        := http
LOCAL_CPPFLAGS += -std=c++14
LOCAL_SRC_FILES     := main.cpp

LOCAL_STATIC_LIBRARIES  := curl
# --------------- log ---------------
LOCAL_LDLIBS:= -llog

include $(BUILD_SHARED_LIBRARY)

