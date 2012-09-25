// beam-capi-test.cpp : Definiert den Einstiegspunkt f�r die Konsolenanwendung.
//

#include <stdio.h>
#include "beamc.h"

#define PATH_SEP ";"

#define BEAM_HOME "C:\\Program Files\\beam-4.10.3\\" 
#define BEAM_LJAR(F) BEAM_HOME ## "lib\\" ## F ## PATH_SEP
#define BEAM_MJAR(F) BEAM_HOME ## "modules\\" ## F ## PATH_SEP

static const char* classpath =
	
	"-Djava.class.path="

    BEAM_LJAR("clibwrapper-jiio-1.2-20090918.jar")
    BEAM_LJAR("commons-codec-1.2.jar")
    BEAM_LJAR("commons-collections-3.1.jar")
    BEAM_LJAR("commons-httpclient-3.1.jar")
    BEAM_LJAR("commons-lang-2.1.jar")
    BEAM_LJAR("commons-logging-1.0.4.jar")
    BEAM_LJAR("commons-pool-1.5.4.jar")
    BEAM_LJAR("gt-api-2.7.4.jar")
    BEAM_LJAR("gt-coverage-2.7.4.jar")
    BEAM_LJAR("gt-cql-2.7.4.jar")
    BEAM_LJAR("gt-data-2.7.4.jar")
    BEAM_LJAR("gt-epsg-hsql-2.7.4.jar")
    BEAM_LJAR("gt-geotiff-2.7.4.jar")
    BEAM_LJAR("gt-main-2.7.4.jar")
    BEAM_LJAR("gt-metadata-2.7.4.jar")
    BEAM_LJAR("gt-opengis-2.7.4.jar")
    BEAM_LJAR("gt-referencing-2.7.4.jar")
    BEAM_LJAR("gt-render-2.7.4.jar")
    BEAM_LJAR("gt-shapefile-2.7.4.jar")
    BEAM_LJAR("gt-wms-2.7.4.jar")
    BEAM_LJAR("gt-xml-2.7.4.jar")
    BEAM_LJAR("hsqldb-1.8.0.7.jar")
    BEAM_LJAR("imageio-ext-tiff-1.0.8.jar")
    BEAM_LJAR("imageio-ext-utilities-1.0.8.jar")
    BEAM_LJAR("jai-codec-1.1.3.jar")
    BEAM_LJAR("jai-core-1.1.3.jar")
    BEAM_LJAR("jai-imageio-1.2-20090918.jar")
    BEAM_LJAR("Jama-1.0.2.jar")
    BEAM_LJAR("javahelp-2.0.02.jar")
    BEAM_LJAR("jcip-annotations-1.0.jar")
    BEAM_LJAR("jcommon-1.0.16.jar")
    BEAM_LJAR("jdom-1.0.jar")
    BEAM_LJAR("jdom-1.1.jar")
    BEAM_LJAR("jfreechart-1.0.13.jar")
    BEAM_LJAR("jide-action-3.3.7.jar")
    BEAM_LJAR("jide-common-3.3.7.jar")
    BEAM_LJAR("jide-components-3.3.7.jar")
    BEAM_LJAR("jide-dock-3.3.7.jar")
    BEAM_LJAR("jide-grids-3.3.7.jar")
    BEAM_LJAR("jnn-1.6.jar")
    BEAM_LJAR("js-1.7R1.jar")
    BEAM_LJAR("jsr-275-1.0-beta-2.jar")
    BEAM_LJAR("jtar-1.0.4.jar")
    BEAM_LJAR("jts-1.11.jar")
    BEAM_LJAR("junit-4.8.2.jar")
    BEAM_LJAR("jython-2.5.2.jar")
    BEAM_LJAR("mlibwrapper-jai-1.1.3.jar")
    BEAM_LJAR("netcdf-4.2.20.jar")
    BEAM_LJAR("nujan-1.4.1.jar")
    BEAM_LJAR("oro-2.0.8.jar")
    BEAM_LJAR( "slf4j-api-1.6.1.jar")
    BEAM_LJAR( "unidataCommon-4.2.20.jar")
    BEAM_LJAR("vecmath-1.3.2.jar")
    BEAM_LJAR("velocity-1.5.jar")
    BEAM_LJAR("xmlpull-1.1.3.1.jar")
    BEAM_LJAR("xpp3-1.1.4c.jar")
    BEAM_LJAR("xpp3_min-1.1.4c.jar")
    BEAM_LJAR("xstream-1.4.2.jar")

    BEAM_MJAR("ceres-binding-0.13.1.jar")
    BEAM_MJAR("ceres-binio-0.13.1.jar")
    BEAM_MJAR("ceres-core-0.13.1.jar")
    BEAM_MJAR("ceres-glayer-0.13.1.jar")
    BEAM_MJAR("ceres-jai-0.13.1.jar")
    BEAM_MJAR("ceres-ui-0.13.1.jar")
    BEAM_MJAR("flint-processor-1.2.jar")
    BEAM_MJAR("beam-aatsr-sst-1.5.1.jar")
    BEAM_MJAR("beam-alos-reader-1.3.1.jar")
    BEAM_MJAR("beam-atsr-reader-1.0.3.jar")
    BEAM_MJAR("beam-avhrr-reader-1.2.1.jar")
    BEAM_MJAR("beam-binning-2.1.1.jar")
    BEAM_MJAR("beam-chris-reader-1.7.1.jar")
    BEAM_MJAR("beam-cluster-analysis-1.1.2.jar")
    BEAM_MJAR("beam-collocation-1.4.1.jar")
    BEAM_MJAR("beam-core-4.10.3.jar")
    BEAM_MJAR("beam-csv-dataio-4.10.3.jar")
    BEAM_MJAR("beam-envisat-reader-1.5.1.jar")
    BEAM_MJAR("beam-flhmci-1.6.103.jar")
    BEAM_MJAR("beam-geotiff-1.2.1.jar")
    BEAM_MJAR("beam-getasse30-reader-1.0.1.jar")
    BEAM_MJAR("beam-gpf-4.10.3.jar")
    BEAM_MJAR("beam-hdf5-writer-1.0.1.jar")
    BEAM_MJAR("beam-landsat-reader-1.3.1.jar")
    BEAM_MJAR("beam-meris-brr-2.3.2.jar")
    BEAM_MJAR("beam-meris-case2-regional-1.5.5.jar")
    BEAM_MJAR("beam-meris-cloud-1.6.1.jar")
    BEAM_MJAR("beam-meris-glint-1.2.2.jar")
    BEAM_MJAR("beam-meris-l2auxdata-1.2.2.jar")
    BEAM_MJAR("beam-meris-ndvi-1.3.1.jar")
    BEAM_MJAR("beam-meris-radiometry-1.1.1.jar")
    BEAM_MJAR("beam-meris-sdr-2.3.2.jar")
    BEAM_MJAR("beam-meris-smac-1.5.204.jar")
    BEAM_MJAR("beam-merisl3-reader-1.2.1.jar")
    BEAM_MJAR("beam-modis-reader-1.3.jar")
    BEAM_MJAR("beam-mosaic-2.3.1.jar")
    BEAM_MJAR("beam-netcdf-1.1.1.jar")
    BEAM_MJAR("beam-obpg-reader-1.3.1.jar")
    BEAM_MJAR("beam-pconvert-1.4.1.jar")
    BEAM_MJAR("beam-pixel-extraction-1.2.jar")
    BEAM_MJAR("beam-processing-4.10.3.jar")
    BEAM_MJAR("beam-scripting-4.10.3.jar")
    BEAM_MJAR("beam-spot-vgt-reader-1.2.1.jar")
    BEAM_MJAR("beam-ui-4.10.3.jar")
    BEAM_MJAR("beam-unmix-1.2.1.jar")
    BEAM_MJAR("beam-visat-4.10.3.jar")
    BEAM_MJAR("beam-visat-rcp-4.10.3.jar")
    BEAM_MJAR("seadas-reader-1.3-20120621-01.jar");


static const char* jvm_options[4];

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

	jvm_options[0] = classpath;
	jvm_options[1] = "-Djava.compiler=NONE"; /* disable hot spot */
	jvm_options[2] = "-Djava.library.path=c:\\mylibs";
	jvm_options[3] = "-verbose:jni";

	if (!beam_create_jvm(jvm_options, 4)) {
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

