#ifndef _BEAMC_H
#define _BEAMC_H 

#include <jni.h>
#include <stdlib.h>
#include <string.h>

typedef jobject Product;

/*
 * Global functions are prefixed by 'beam'. 
 */

jboolean beam_is_jvm_created();
jboolean beam_create_jvm(const char* option_strings[], int option_count); 
jboolean beam_create_jvm_with_defaults(); 
jboolean beam_destroy_jvm(); 

Product beam_read_product(const char* file_path);
Product beam_create_product(const char* op_name, const char** parameters, Product source_product, ...);
void beam_release_object(jobject* object);

/*
 * Product functions are prefixed by 'product'. 
 */

char* product_get_name(Product product);
int product_get_num_bands(Product product);
char** product_get_band_names(Product product);

#define LOG_OFF    0
#define LOG_ERROR 10
#define LOG_WARN  20
#define LOG_INFO  30
#define LOG_DEBUG 40
static int beam_log_level = LOG_INFO;
void beam_log(int level, const char* format, ...);

#define _BEAM_DEBUG 1

#ifdef _BEAM_DEBUG
#define BEAM_TRACE printf
#else
#define BEAM_TRACE //
#endif

#endif /* _BEAMC_H */ 