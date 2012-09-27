import os

path = os.getenv('PATH')
print("path =", path)

import beampy

product = beampy.beam_read_product('C:\\EOData\\MERIS\\MER_RR__1alpen.N1')
print("product =", product)
product_name = beampy.product_get_name(product)
num_bands = beampy.product_get_num_bands(product)
band_names = beampy.product_get_band_names(product)

pass
