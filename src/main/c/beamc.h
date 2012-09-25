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


#endif /* _BEAMC_H */ 