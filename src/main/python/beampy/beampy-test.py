import os
import beampy

path = os.getenv('PATH')
print("path =", path)

product = beampy.read_product('C:\\EOData\\MERIS\\MER_RR__1alpen.N1')
product_name = beampy.product_get_name(product)
num_bands = beampy.product_get_num_bands(product)
band_names = beampy.product_get_band_names(product)

print('product_name =', product_name)
print('num_bands =', num_bands)
print('band_names =', band_names)

pass
