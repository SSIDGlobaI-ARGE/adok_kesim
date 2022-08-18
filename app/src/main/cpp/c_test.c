#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>

static const int stateA = 0;
static const int stateB = 10;
static const int stateC = 11;
static const int stateD = 1;
#define VALUE_MAX 30
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,"JNILOG", __VA_ARGS__)

//JNIEXPORT jstring JNICALL
//Java_com_ssidglobal_machbee_1gezderi_ControlPanel_024AsyncCaller_stringFromJNI(JNIEnv *env,jobject thiz,int old_state) {
////
////    char path[VALUE_MAX];
////    char path2[VALUE_MAX];
////    char value_str[3];
////    char value_str100[3];
////    char oldval[3] = "";
////    char formatted[100] = "";
////    int fd;
////    int fd2;
////    char state[3];
////
////    snprintf(path, VALUE_MAX, "/sys/class/gpio/gpio99/value");
////    snprintf(path2, VALUE_MAX, "/sys/class/gpio/gpio100/value");
////    fd = open(path, O_RDONLY);
////    if (-1 == fd) {
////        fprintf(stderr, "Failed to open gpio value for reading!\n");
////        return (-1);
////    }
////
////    if (-1 == read(fd, value_str, 3)) {
////        fprintf(stderr, "Failed to read value!\n");
////        return (-1);
////    }
////    int value_str_int = value_str[0] - '0';
////
////    close(fd);
////    fd2 = open(path2, O_RDONLY);
////    if (-1 == fd2) {
////        fprintf(stderr, "Failed to open gpio value for reading!\n");
////        return (-1);
////    }
////
////    if (-1 == read(fd2, value_str100, 3)) {
////        fprintf(stderr, "Failed to read value!\n");
////        return (-1);
////    }
////    int value_str_int100 = value_str100[0] - '0';
////
////    close(fd2);
////
////    int current_state = (value_str_int * 10) + value_str_int100;
////
////    strncpy(state, "0", 3);
////    if (old_state != current_state) {
////
////        switch (old_state) {
////            case stateA:
////                if (current_state == stateB) {
////                    strncpy(state, "1", 3);
////
////
////                } else if (current_state == stateD) {
////                    strncpy(state, "2", 3);
////
////                }else if (current_state == stateC) {
////                    strncpy(state, "3", 3);
////                }
////                break;
////            case stateB:
////
////                if (current_state == stateC) {
////                    strncpy(state, "1", 3);
////
////                } else if (current_state == stateA) {
////                    strncpy(state, "2", 3);
////
////                } else if (current_state == stateD) {
////                    strncpy(state, "3", 3);
////                }
////                break;
////            case stateC:
////
////                if (current_state == stateD) {
////                    strncpy(state, "1", 3);
////
////                } else if (current_state == stateB) {
////                    strncpy(state, "2", 3);
////                } else if (current_state == stateA) {
////                    strncpy(state, "3", 3);
////                }
////                break;
////            case stateD:
////                if (current_state == stateA) {
////                    strncpy(state, "1", 3);
////
////                } else if (current_state == stateC) {
////                    strncpy(state, "2", 3);
////
////                }else if (current_state == stateB) {
////                    strncpy(state, "3", 3);
////
////                }
////                break;
////            default:
////                break;
////        }
////    }
////
////    strncat(formatted,  &value_str[0], 1);
////    strncat(formatted,&value_str100[0],1);
////    strncat(formatted,&state[0],1);
//////    snprintf(formatted, 3, "%s", &value_str[0] + value_str100[0] + state[0]);
////
////
////    return (*env)->NewStringUTF(env, formatted);
//
//}

JNIEXPORT jstring JNICALL
Java_com_ardayucesan_gezderi_1uretim_ui_controlpanel_PanelPresenter_stringFromJNI(JNIEnv *env,jobject thiz,jint old_state) {
    char path[VALUE_MAX];
    char path2[VALUE_MAX];
    char value_str[3];
    char value_str100[3];
    char formatted[100] = "";
    int fd;
    int fd2;
    char state[3];

    snprintf(path, VALUE_MAX, "/sys/class/gpio/gpio99/value");
    snprintf(path2, VALUE_MAX, "/sys/class/gpio/gpio100/value");
    fd = open(path, O_RDONLY);
    if (-1 == fd) {
        fprintf(stderr, "Failed to open gpio value for reading!\n");
        return (-1);
    }

    if (-1 == read(fd, value_str, 3)) {
        fprintf(stderr, "Failed to read value!\n");
        return (-1);
    }

    int value_str_int = value_str[0] - '0';

    close(fd);
    fd2 = open(path2, O_RDONLY);
    if (-1 == fd2) {
        fprintf(stderr, "Failed to open gpio value for reading!\n");
        return (-1);
    }

    if (-1 == read(fd2, value_str100, 3)) {
        fprintf(stderr, "Failed to read value!\n");
        return (-1);
    }
    int value_str_int100 = value_str100[0] - '0';

    close(fd2);

    int current_state = (value_str_int * 10) + value_str_int100;

    strncpy(state, "0", 3);
    if (old_state != current_state) {

        switch (old_state) {
            case stateA:
                if (current_state == stateB) {
                    strncpy(state, "1", 3);


                } else if (current_state == stateD) {
                    strncpy(state, "2", 3);

                }else if (current_state == stateC) {
                    strncpy(state, "3", 3);
                }
                break;
            case stateB:

                if (current_state == stateC) {
                    strncpy(state, "1", 3);

                } else if (current_state == stateA) {
                    strncpy(state, "2", 3);

                } else if (current_state == stateD) {
                    strncpy(state, "3", 3);
                }
                break;
            case stateC:

                if (current_state == stateD) {
                    strncpy(state, "1", 3);

                } else if (current_state == stateB) {
                    strncpy(state, "2", 3);
                } else if (current_state == stateA) {
                    strncpy(state, "3", 3);
                }
                break;
            case stateD:
                if (current_state == stateA) {
                    strncpy(state, "1", 3);

                } else if (current_state == stateC) {
                    strncpy(state, "2", 3);

                }else if (current_state == stateB) {
                    strncpy(state, "3", 3);

                }
                break;
            default:
                break;
        }
    }

    strncat(formatted,  &value_str[0], 1);
    strncat(formatted,&value_str100[0],1);
    strncat(formatted,&state[0],1);
//    snprintf(formatted, 3, "%s", &value_str[0] + value_str100[0] + state[0]);

    return (*env)->NewStringUTF(env, formatted);
}
