import sys
import numpy

from beampy import String
from beampy import Product
from beampy import ProductData
from beampy import ProductIO
from beampy import ProductUtils

if len(sys.argv) != 2:
    print("usage: %s <file>" % sys.argv[0]);
    sys.exit(1)

sourceProduct = ProductIO.readProduct(sys.argv[1])
b1 = sourceProduct.getBand('reflec_5')
b2 = sourceProduct.getBand('reflec_7')
b3 = sourceProduct.getBand('reflec_9')
w1 = b1.getSpectralWavelength()
w2 = b2.getSpectralWavelength()
w3 = b3.getSpectralWavelength()
a = (w2 - w1) / (w3 - w1)
k = 1.03

width = sourceProduct.getSceneRasterWidth()
height = sourceProduct.getSceneRasterHeight()
targetName = 'FLH_Product'
targetProduct = Product.newProduct(targetName, 'test', width, height)
targetBand = targetProduct.addNewBand('FLH', ProductData.TYPE_FLOAT32)
ProductUtils.copyGeoCoding(sourceProduct, targetProduct)
targetProduct.setProductWriter(ProductIO.getProductWriter('GeoTIFF'))
targetProduct.writeHeader(String.newString(targetName + '.tif'))

r1 = numpy.zeros(width, dtype=numpy.float32)
r2 = numpy.zeros(width, dtype=numpy.float32)
r3 = numpy.zeros(width, dtype=numpy.float32)

for y in range(height):
    b1.readPixelsFloat(0, y, width, 1, r1)
    b2.readPixelsFloat(0, y, width, 1, r2)
    b2.readPixelsFloat(0, y, width, 1, r3)
    print("processing line ", y, " of ", height)
    FLH = r2 – k * (r1 + a * (r3 – r1))
    targetBand.writePixelsFloat(0, y, width, 1, FLH)

targetProduct.closeIO()
