// beam-capi-test.cpp : Definiert den Einstiegspunkt f�r die Konsolenanwendung.
//

#include <stdio.h>
#include "beamc.h"

int main(int argc, char** argv)
{
	jobject product;
	const char* product_path;
	char* product_name;
	char** band_names;
	int num_bands;

	if (argc <= 1) {
		fprintf(stderr, "usage: beam-capi-test <product-file>");
		return 1;
	}

	product_path = argv[1]; 

	if (!beam_create_jvm_with_defaults()) {
		return 2;
	}

	product = beam_read_product(argv[1]);
	if (product == NULL) {
		return 3;
	}

	product_name = product_get_name(product);
    num_bands = product_get_num_bands(product);
    band_names = product_get_band_names(product);

	{
		int i;

		printf("product_path = %s\n", product_path);
		printf("product_name = %s\n", product_name);
		printf("num_bands = %d\n", num_bands);

		for (i = 0; i < num_bands; i++) {
			printf("band_names[%d] = %s\n", i, band_names[i]);
		}
	}

	beam_release_object(&product);

	if (!beam_destroy_jvm()) {
		return 4;
	}

	return 0;
}

