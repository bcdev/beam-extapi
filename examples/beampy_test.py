import sys
import beampy
import numpy
import itertools as it

from beampy import String
from beampy import Product
from beampy import ProductIO
from beampy import ProductUtils

def readLine(band, y, w, buf):
    print('A')
    buf = band.readPixelsFloat(0, y, 0, w, buf)
    print('B')
    return numpy.frombuffer(buf, 'float32')

if len(sys.argv) != 2:
    printf("usage: %s <file>", sys.argv[0]);
    sys.exit(1)

product = ProductIO.readProduct(sys.argv[1])
width = product.getSceneRasterWidth()
height = product.getSceneRasterHeight()
name = product.getName()
desc = product.getDescription()
band_names = product.getBandNames()

print("Product: %s, %d x %d pixels, %s" % (name, width, height, desc))
print("Bands:   %s" % (band_names))

r7 = product.getBand('radiance_7')
r10 = product.getBand('radiance_10')
