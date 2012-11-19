from beampy import *

class Shape:

    def __init__(self, obj):
        self.__obj = obj


class MapTransform:

    def __init__(self, obj):
        self.__obj = obj


class ImageGeometry:

    def __init__(self, obj):
        self.__obj = obj

    def newImageGeometry(bounds, mapCrs, image2map):
        return ImageGeometry(ImageGeometry_newImageGeometry(bounds.__obj, mapCrs.__obj, image2map.__obj))

    def getImage2MapTransform(self):
        return AffineTransform(self.ImageGeometry_getImage2MapTransform(self.__obj))

    def getImageRect(self):
        return Rectangle(self.ImageGeometry_getImageRect(self.__obj))

    def getMapCrs(self):
        return CoordinateReferenceSystem(self.ImageGeometry_getMapCrs(self.__obj))

    def changeYAxisDirection(self):
        self.ImageGeometry_changeYAxisDirection(self.__obj)
        return

    def calculateEastingNorthing(sourceProduct, targetCrs, referencePixelX, referencePixelY, pixelSizeX, pixelSizeY):
        return Point2D(ImageGeometry_calculateEastingNorthing(sourceProduct.__obj, targetCrs.__obj, referencePixelX, referencePixelY, pixelSizeX, pixelSizeY))

    def calculateProductSize(sourceProduct, targetCrs, pixelSizeX, pixelSizeY):
        return Rectangle(ImageGeometry_calculateProductSize(sourceProduct.__obj, targetCrs.__obj, pixelSizeX, pixelSizeY))

    def createTargetGeometry(sourceProduct, targetCrs, pixelSizeX, pixelSizeY, width, height, orientation, easting, northing, referencePixelX, referencePixelY):
        return ImageGeometry(ImageGeometry_createTargetGeometry(sourceProduct.__obj, targetCrs.__obj, pixelSizeX.__obj, pixelSizeY.__obj, width.__obj, height.__obj, orientation.__obj, easting.__obj, northing.__obj, referencePixelX.__obj, referencePixelY.__obj))

    def createCollocationTargetGeometry(targetProduct, collocationProduct):
        return ImageGeometry(ImageGeometry_createCollocationTargetGeometry(targetProduct.__obj, collocationProduct.__obj))


class GeoCoding:

    def __init__(self, obj):
        self.__obj = obj

    def isCrossingMeridianAt180(self):
        return self.GeoCoding_isCrossingMeridianAt180(self.__obj)

    def canGetPixelPos(self):
        return self.GeoCoding_canGetPixelPos(self.__obj)

    def canGetGeoPos(self):
        return self.GeoCoding_canGetGeoPos(self.__obj)

    def getPixelPos(self, geoPos, pixelPos):
        return PixelPos(self.GeoCoding_getPixelPos(self.__obj, geoPos.__obj, pixelPos.__obj))

    def getGeoPos(self, pixelPos, geoPos):
        return GeoPos(self.GeoCoding_getGeoPos(self.__obj, pixelPos.__obj, geoPos.__obj))

    def dispose(self):
        self.GeoCoding_dispose(self.__obj)
        return

    def getImageCRS(self):
        return CoordinateReferenceSystem(self.GeoCoding_getImageCRS(self.__obj))

    def getMapCRS(self):
        return CoordinateReferenceSystem(self.GeoCoding_getMapCRS(self.__obj))

    def getGeoCRS(self):
        return CoordinateReferenceSystem(self.GeoCoding_getGeoCRS(self.__obj))

    def getImageToMapTransform(self):
        return MathTransform(self.GeoCoding_getImageToMapTransform(self.__obj))


class Parser:

    def __init__(self, obj):
        self.__obj = obj


class ProductData:

    def __init__(self, obj):
        self.__obj = obj

    def createInstance(type):
        return ProductData(ProductData_createInstance1(type))

    def createInstance(type, numElems):
        return ProductData(ProductData_createInstance2(type, numElems))

    def createInstance(type, data):
        return ProductData(ProductData_createInstance3(type, data.__obj))

    def createInstance(elems):
        return ProductData(ProductData_createInstance5(elems))

    def createUnsignedInstance(elems):
        return ProductData(ProductData_createUnsignedInstance1(elems))

    def createInstance(elems):
        return ProductData(ProductData_createInstance10(elems))

    def createUnsignedInstance(elems):
        return ProductData(ProductData_createUnsignedInstance3(elems))

    def createInstance(elems):
        return ProductData(ProductData_createInstance8(elems))

    def createUnsignedInstance(elems):
        return ProductData(ProductData_createUnsignedInstance2(elems))

    def createInstance(elems):
        return ProductData(ProductData_createInstance9(elems))

    def createInstance(strData):
        return ProductData(ProductData_createInstance4(strData))

    def createInstance(elems):
        return ProductData(ProductData_createInstance7(elems))

    def createInstance(elems):
        return ProductData(ProductData_createInstance6(elems))

    def getType(self):
        return self.ProductData_getType1(self.__obj)

    def getElemSize(type):
        return ProductData_getElemSize2(type)

    def getElemSize(self):
        return self.ProductData_getElemSize1(self.__obj)

    def getTypeString(type):
        return ProductData_getTypeString2(type)

    def getType(type):
        return ProductData_getType2(type)

    def getTypeString(self):
        return self.ProductData_getTypeString1(self.__obj)

    def isInt(self):
        return self.ProductData_isInt(self.__obj)

    def isIntType(type):
        return ProductData_isIntType(type)

    def isSigned(self):
        return self.ProductData_isSigned(self.__obj)

    def isUnsigned(self):
        return self.ProductData_isUnsigned(self.__obj)

    def isUIntType(type):
        return ProductData_isUIntType(type)

    def isFloatingPointType(type):
        return ProductData_isFloatingPointType(type)

    def isScalar(self):
        return self.ProductData_isScalar(self.__obj)

    def getNumElems(self):
        return self.ProductData_getNumElems(self.__obj)

    def getElemInt(self):
        return self.ProductData_getElemInt(self.__obj)

    def getElemUInt(self):
        return self.ProductData_getElemUInt(self.__obj)

    def getElemFloat(self):
        return self.ProductData_getElemFloat(self.__obj)

    def getElemDouble(self):
        return self.ProductData_getElemDouble(self.__obj)

    def getElemString(self):
        return self.ProductData_getElemString(self.__obj)

    def getElemBoolean(self):
        return self.ProductData_getElemBoolean(self.__obj)

    def getElemIntAt(self, index):
        return self.ProductData_getElemIntAt(self.__obj, index)

    def getElemUIntAt(self, index):
        return self.ProductData_getElemUIntAt(self.__obj, index)

    def getElemFloatAt(self, index):
        return self.ProductData_getElemFloatAt(self.__obj, index)

    def getElemDoubleAt(self, index):
        return self.ProductData_getElemDoubleAt(self.__obj, index)

    def getElemStringAt(self, index):
        return self.ProductData_getElemStringAt(self.__obj, index)

    def getElemBooleanAt(self, index):
        return self.ProductData_getElemBooleanAt(self.__obj, index)

    def setElemInt(self, value):
        self.ProductData_setElemInt(self.__obj, value)
        return

    def setElemUInt(self, value):
        self.ProductData_setElemUInt(self.__obj, value)
        return

    def setElemFloat(self, value):
        self.ProductData_setElemFloat(self.__obj, value)
        return

    def setElemDouble(self, value):
        self.ProductData_setElemDouble(self.__obj, value)
        return

    def setElemString(self, value):
        self.ProductData_setElemString(self.__obj, value)
        return

    def setElemBoolean(self, value):
        self.ProductData_setElemBoolean(self.__obj, value)
        return

    def setElemIntAt(self, index, value):
        self.ProductData_setElemIntAt(self.__obj, index, value)
        return

    def setElemUIntAt(self, index, value):
        self.ProductData_setElemUIntAt(self.__obj, index, value)
        return

    def setElemFloatAt(self, index, value):
        self.ProductData_setElemFloatAt(self.__obj, index, value)
        return

    def setElemDoubleAt(self, index, value):
        self.ProductData_setElemDoubleAt(self.__obj, index, value)
        return

    def setElemStringAt(self, index, value):
        self.ProductData_setElemStringAt(self.__obj, index, value)
        return

    def setElemBooleanAt(self, index, value):
        self.ProductData_setElemBooleanAt(self.__obj, index, value)
        return

    def getElems(self):
        return Object(self.ProductData_getElems(self.__obj))

    def setElems(self, data):
        self.ProductData_setElems(self.__obj, data.__obj)
        return

    def readFrom(self, input):
        self.ProductData_readFrom4(self.__obj, input.__obj)
        return

    def readFrom(self, pos, input):
        self.ProductData_readFrom3(self.__obj, pos, input.__obj)
        return

    def readFrom(self, startPos, numElems, input):
        self.ProductData_readFrom1(self.__obj, startPos, numElems, input.__obj)
        return

    def readFrom(self, startPos, numElems, input, inputPos):
        self.ProductData_readFrom2(self.__obj, startPos, numElems, input.__obj, inputPos)
        return

    def writeTo(self, output):
        self.ProductData_writeTo4(self.__obj, output.__obj)
        return

    def writeTo(self, pos, output):
        self.ProductData_writeTo3(self.__obj, pos, output.__obj)
        return

    def writeTo(self, startPos, numElems, output):
        self.ProductData_writeTo1(self.__obj, startPos, numElems, output.__obj)
        return

    def writeTo(self, startPos, numElems, output, outputPos):
        self.ProductData_writeTo2(self.__obj, startPos, numElems, output.__obj, outputPos)
        return

    def toString(self):
        return self.ProductData_toString(self.__obj)

    def hashCode(self):
        return self.ProductData_hashCode(self.__obj)

    def equals(self, other):
        return self.ProductData_equals(self.__obj, other.__obj)

    def equalElems(self, other):
        return self.ProductData_equalElems(self.__obj, other.__obj)

    def dispose(self):
        self.ProductData_dispose(self.__obj)
        return


class AffineTransform:

    def __init__(self, obj):
        self.__obj = obj


class Mask:

    def __init__(self, obj):
        self.__obj = obj


class Double:

    def __init__(self, obj):
        self.__obj = obj


class IndexCoding:

    def __init__(self, obj):
        self.__obj = obj

    def newIndexCoding(name):
        return IndexCoding(IndexCoding_newIndexCoding(name))

    def getIndex(self, name):
        return MetadataAttribute(self.IndexCoding_getIndex(self.__obj, name))

    def getIndexNames(self):
        return self.IndexCoding_getIndexNames(self.__obj)

    def addIndex(self, name, value, description):
        return MetadataAttribute(self.IndexCoding_addIndex(self.__obj, name, value, description))

    def getIndexValue(self, name):
        return self.IndexCoding_getIndexValue(self.__obj, name)

    def acceptVisitor(self, visitor):
        self.IndexCoding_acceptVisitor(self.__obj, visitor.__obj)
        return

    def addElement(self, element):
        self.IndexCoding_addElement(self.__obj, element.__obj)
        return

    def addAttribute(self, attribute):
        self.IndexCoding_addAttribute(self.__obj, attribute.__obj)
        return

    def addSample(self, name, value, description):
        return MetadataAttribute(self.IndexCoding_addSample(self.__obj, name, value, description))

    def getSampleCount(self):
        return self.IndexCoding_getSampleCount(self.__obj)

    def getSampleName(self, index):
        return self.IndexCoding_getSampleName(self.__obj, index)

    def getSampleValue(self, index):
        return self.IndexCoding_getSampleValue(self.__obj, index)

    def getElementGroup(self):
        return ProductNodeGroup(self.IndexCoding_getElementGroup(self.__obj))

    def getParentElement(self):
        return MetadataElement(self.IndexCoding_getParentElement(self.__obj))

    def addElementAt(self, element, index):
        self.IndexCoding_addElementAt(self.__obj, element.__obj, index)
        return

    def removeElement(self, element):
        return self.IndexCoding_removeElement(self.__obj, element.__obj)

    def getNumElements(self):
        return self.IndexCoding_getNumElements(self.__obj)

    def getElementAt(self, index):
        return MetadataElement(self.IndexCoding_getElementAt(self.__obj, index))

    def getElementNames(self):
        return self.IndexCoding_getElementNames(self.__obj)

    def getElements(self):
        return MetadataElement(self.IndexCoding_getElements(self.__obj))

    def getElement(self, name):
        return MetadataElement(self.IndexCoding_getElement(self.__obj, name))

    def containsElement(self, name):
        return self.IndexCoding_containsElement(self.__obj, name)

    def getElementIndex(self, element):
        return self.IndexCoding_getElementIndex(self.__obj, element.__obj)

    def removeAttribute(self, attribute):
        return self.IndexCoding_removeAttribute(self.__obj, attribute.__obj)

    def getNumAttributes(self):
        return self.IndexCoding_getNumAttributes(self.__obj)

    def getAttributeAt(self, index):
        return MetadataAttribute(self.IndexCoding_getAttributeAt(self.__obj, index))

    def getAttributeNames(self):
        return self.IndexCoding_getAttributeNames(self.__obj)

    def getAttributes(self):
        return MetadataAttribute(self.IndexCoding_getAttributes(self.__obj))

    def getAttribute(self, name):
        return MetadataAttribute(self.IndexCoding_getAttribute(self.__obj, name))

    def containsAttribute(self, name):
        return self.IndexCoding_containsAttribute(self.__obj, name)

    def getAttributeIndex(self, attribute):
        return self.IndexCoding_getAttributeIndex(self.__obj, attribute.__obj)

    def getAttributeDouble(self, name, defaultValue):
        return self.IndexCoding_getAttributeDouble(self.__obj, name, defaultValue)

    def getAttributeUTC(self, name, defaultValue):
        return ProductData_UTC(self.IndexCoding_getAttributeUTC(self.__obj, name, defaultValue.__obj))

    def getAttributeInt(self, name, defaultValue):
        return self.IndexCoding_getAttributeInt(self.__obj, name, defaultValue)

    def setAttributeInt(self, name, value):
        self.IndexCoding_setAttributeInt(self.__obj, name, value)
        return

    def setAttributeDouble(self, name, value):
        self.IndexCoding_setAttributeDouble(self.__obj, name, value)
        return

    def setAttributeUTC(self, name, value):
        self.IndexCoding_setAttributeUTC(self.__obj, name, value.__obj)
        return

    def getAttributeString(self, name, defaultValue):
        return self.IndexCoding_getAttributeString(self.__obj, name, defaultValue)

    def setAttributeString(self, name, value):
        self.IndexCoding_setAttributeString(self.__obj, name, value)
        return

    def setModified(self, modified):
        self.IndexCoding_setModified(self.__obj, modified)
        return

    def createDeepClone(self):
        return MetadataElement(self.IndexCoding_createDeepClone(self.__obj))

    def dispose(self):
        self.IndexCoding_dispose(self.__obj)
        return

    def getOwner(self):
        return ProductNode(self.IndexCoding_getOwner(self.__obj))

    def getName(self):
        return self.IndexCoding_getName(self.__obj)

    def setName(self, name):
        self.IndexCoding_setName(self.__obj, name)
        return

    def getDescription(self):
        return self.IndexCoding_getDescription(self.__obj)

    def setDescription(self, description):
        self.IndexCoding_setDescription(self.__obj, description)
        return

    def isModified(self):
        return self.IndexCoding_isModified(self.__obj)

    def toString(self):
        return self.IndexCoding_toString(self.__obj)

    def isValidNodeName(name):
        return IndexCoding_isValidNodeName(name)

    def getProduct(self):
        return Product(self.IndexCoding_getProduct(self.__obj))

    def getProductReader(self):
        return ProductReader(self.IndexCoding_getProductReader(self.__obj))

    def getProductWriter(self):
        return ProductWriter(self.IndexCoding_getProductWriter(self.__obj))

    def getDisplayName(self):
        return self.IndexCoding_getDisplayName(self.__obj)

    def getProductRefString(self):
        return self.IndexCoding_getProductRefString(self.__obj)

    def updateExpression(self, oldExternalName, newExternalName):
        self.IndexCoding_updateExpression(self.__obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        self.IndexCoding_removeFromFile(self.__obj, productWriter.__obj)
        return


class Term:

    def __init__(self, obj):
        self.__obj = obj


class RasterDataNode:

    def __init__(self, obj):
        self.__obj = obj


class PixelPos:

    def __init__(self, obj):
        self.__obj = obj

    def newPixelPos():
        return PixelPos(PixelPos_newPixelPos1())

    def newPixelPos(x, y):
        return PixelPos(PixelPos_newPixelPos2(x, y))

    def isValid(self):
        return self.PixelPos_isValid(self.__obj)

    def setInvalid(self):
        self.PixelPos_setInvalid(self.__obj)
        return

    def getX(self):
        return self.PixelPos_getX(self.__obj)

    def getY(self):
        return self.PixelPos_getY(self.__obj)

    def setLocation(self, arg0, arg1):
        self.PixelPos_setLocation1(self.__obj, arg0, arg1)
        return

    def setLocation(self, arg0, arg1):
        self.PixelPos_setLocation2(self.__obj, arg0, arg1)
        return

    def toString(self):
        return self.PixelPos_toString(self.__obj)

    def setLocation(self, arg0):
        self.PixelPos_setLocation3(self.__obj, arg0.__obj)
        return

    def distanceSq(arg0, arg1, arg2, arg3):
        return PixelPos_distanceSq2(arg0, arg1, arg2, arg3)

    def distance(arg0, arg1, arg2, arg3):
        return PixelPos_distance2(arg0, arg1, arg2, arg3)

    def distanceSq(self, arg0, arg1):
        return self.PixelPos_distanceSq1(self.__obj, arg0, arg1)

    def distanceSq(self, arg0):
        return self.PixelPos_distanceSq3(self.__obj, arg0.__obj)

    def distance(self, arg0, arg1):
        return self.PixelPos_distance1(self.__obj, arg0, arg1)

    def distance(self, arg0):
        return self.PixelPos_distance3(self.__obj, arg0.__obj)

    def clone(self):
        return Object(self.PixelPos_clone(self.__obj))

    def hashCode(self):
        return self.PixelPos_hashCode(self.__obj)

    def equals(self, arg0):
        return self.PixelPos_equals(self.__obj, arg0.__obj)


class Product_AutoGrouping:

    def __init__(self, obj):
        self.__obj = obj


class ImageOutputStream:

    def __init__(self, obj):
        self.__obj = obj


class Stx:

    def __init__(self, obj):
        self.__obj = obj


class Rectangle:

    def __init__(self, obj):
        self.__obj = obj


class ProductNode:

    def __init__(self, obj):
        self.__obj = obj


class Dimension:

    def __init__(self, obj):
        self.__obj = obj


class AngularDirection:

    def __init__(self, obj):
        self.__obj = obj

    def newAngularDirection(azimuth, zenith):
        return AngularDirection(AngularDirection_newAngularDirection(azimuth, zenith))

    def equals(self, obj):
        return self.AngularDirection_equals(self.__obj, obj.__obj)

    def toString(self):
        return self.AngularDirection_toString(self.__obj)


class SimpleFeatureType:

    def __init__(self, obj):
        self.__obj = obj


class SampleCoding:

    def __init__(self, obj):
        self.__obj = obj


class Object:

    def __init__(self, obj):
        self.__obj = obj


class ProductReader:

    def __init__(self, obj):
        self.__obj = obj

    def getReaderPlugIn(self):
        return ProductReaderPlugIn(self.ProductReader_getReaderPlugIn(self.__obj))

    def getInput(self):
        return Object(self.ProductReader_getInput(self.__obj))

    def getSubsetDef(self):
        return ProductSubsetDef(self.ProductReader_getSubsetDef(self.__obj))

    def readProductNodes(self, input, subsetDef):
        return Product(self.ProductReader_readProductNodes(self.__obj, input.__obj, subsetDef.__obj))

    def readBandRasterData(self, destBand, destOffsetX, destOffsetY, destWidth, destHeight, destBuffer, pm):
        self.ProductReader_readBandRasterData(self.__obj, destBand.__obj, destOffsetX, destOffsetY, destWidth, destHeight, destBuffer.__obj, pm.__obj)
        return

    def close(self):
        self.ProductReader_close(self.__obj)
        return


class ProductReaderPlugIn:

    def __init__(self, obj):
        self.__obj = obj


class Integer:

    def __init__(self, obj):
        self.__obj = obj


class ProductData_UTC:

    def __init__(self, obj):
        self.__obj = obj


class Band:

    def __init__(self, obj):
        self.__obj = obj

    def newBand(name, dataType, width, height):
        return Band(Band_newBand(name, dataType, width, height))

    def getFlagCoding(self):
        return FlagCoding(self.Band_getFlagCoding(self.__obj))

    def isFlagBand(self):
        return self.Band_isFlagBand(self.__obj)

    def getIndexCoding(self):
        return IndexCoding(self.Band_getIndexCoding(self.__obj))

    def isIndexBand(self):
        return self.Band_isIndexBand(self.__obj)

    def getSampleCoding(self):
        return SampleCoding(self.Band_getSampleCoding(self.__obj))

    def setSampleCoding(self, sampleCoding):
        self.Band_setSampleCoding(self.__obj, sampleCoding.__obj)
        return

    def getSpectralBandIndex(self):
        return self.Band_getSpectralBandIndex(self.__obj)

    def setSpectralBandIndex(self, spectralBandIndex):
        self.Band_setSpectralBandIndex(self.__obj, spectralBandIndex)
        return

    def getSpectralWavelength(self):
        return self.Band_getSpectralWavelength(self.__obj)

    def setSpectralWavelength(self, spectralWavelength):
        self.Band_setSpectralWavelength(self.__obj, spectralWavelength)
        return

    def getSpectralBandwidth(self):
        return self.Band_getSpectralBandwidth(self.__obj)

    def setSpectralBandwidth(self, spectralBandwidth):
        self.Band_setSpectralBandwidth(self.__obj, spectralBandwidth)
        return

    def getSolarFlux(self):
        return self.Band_getSolarFlux(self.__obj)

    def setSolarFlux(self, solarFlux):
        self.Band_setSolarFlux(self.__obj, solarFlux)
        return

    def acceptVisitor(self, visitor):
        self.Band_acceptVisitor(self.__obj, visitor.__obj)
        return

    def toString(self):
        return self.Band_toString(self.__obj)

    def removeFromFile(self, productWriter):
        self.Band_removeFromFile(self.__obj, productWriter.__obj)
        return

    def dispose(self):
        self.Band_dispose(self.__obj)
        return

    def getSceneRasterData(self):
        return ProductData(self.Band_getSceneRasterData(self.__obj))

    def getPixelInt(self, x, y):
        return self.Band_getPixelInt(self.__obj, x, y)

    def getPixelFloat(self, x, y):
        return self.Band_getPixelFloat(self.__obj, x, y)

    def getPixelDouble(self, x, y):
        return self.Band_getPixelDouble(self.__obj, x, y)

    def setPixelInt(self, x, y, pixelValue):
        self.Band_setPixelInt(self.__obj, x, y, pixelValue)
        return

    def setPixelFloat(self, x, y, pixelValue):
        self.Band_setPixelFloat(self.__obj, x, y, pixelValue)
        return

    def setPixelDouble(self, x, y, pixelValue):
        self.Band_setPixelDouble(self.__obj, x, y, pixelValue)
        return

    def setPixels(self, x, y, w, h, pixels):
        self.Band_setPixelsInt(self.__obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        self.Band_setPixelsFloat(self.__obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        self.Band_setPixelsDouble(self.__obj, x, y, w, h, pixels)
        return

    def ensureRasterData(self):
        self.Band_ensureRasterData(self.__obj)
        return

    def unloadRasterData(self):
        self.Band_unloadRasterData(self.__obj)
        return

    def getViewModeId(self, bandName):
        return self.Band_getViewModeId(self.__obj, bandName)

    def getSceneRasterWidth(self):
        return self.Band_getSceneRasterWidth(self.__obj)

    def getSceneRasterHeight(self):
        return self.Band_getSceneRasterHeight(self.__obj)

    def getRasterWidth(self):
        return self.Band_getRasterWidth(self.__obj)

    def getRasterHeight(self):
        return self.Band_getRasterHeight(self.__obj)

    def setModified(self, modified):
        self.Band_setModified(self.__obj, modified)
        return

    def getGeoCoding(self):
        return GeoCoding(self.Band_getGeoCoding(self.__obj))

    def setGeoCoding(self, geoCoding):
        self.Band_setGeoCoding(self.__obj, geoCoding.__obj)
        return

    def getPointing(self):
        return Pointing(self.Band_getPointing(self.__obj))

    def canBeOrthorectified(self):
        return self.Band_canBeOrthorectified(self.__obj)

    def isFloatingPointType(self):
        return self.Band_isFloatingPointType(self.__obj)

    def getGeophysicalDataType(self):
        return self.Band_getGeophysicalDataType(self.__obj)

    def getScalingFactor(self):
        return self.Band_getScalingFactor(self.__obj)

    def setScalingFactor(self, scalingFactor):
        self.Band_setScalingFactor(self.__obj, scalingFactor)
        return

    def getScalingOffset(self):
        return self.Band_getScalingOffset(self.__obj)

    def setScalingOffset(self, scalingOffset):
        self.Band_setScalingOffset(self.__obj, scalingOffset)
        return

    def isLog10Scaled(self):
        return self.Band_isLog10Scaled(self.__obj)

    def setLog10Scaled(self, log10Scaled):
        self.Band_setLog10Scaled(self.__obj, log10Scaled)
        return

    def isScalingApplied(self):
        return self.Band_isScalingApplied(self.__obj)

    def isValidMaskProperty(propertyName):
        return Band_isValidMaskProperty(propertyName)

    def isNoDataValueSet(self):
        return self.Band_isNoDataValueSet(self.__obj)

    def clearNoDataValue(self):
        self.Band_clearNoDataValue(self.__obj)
        return

    def isNoDataValueUsed(self):
        return self.Band_isNoDataValueUsed(self.__obj)

    def setNoDataValueUsed(self, noDataValueUsed):
        self.Band_setNoDataValueUsed(self.__obj, noDataValueUsed)
        return

    def getNoDataValue(self):
        return self.Band_getNoDataValue(self.__obj)

    def setNoDataValue(self, noDataValue):
        self.Band_setNoDataValue(self.__obj, noDataValue)
        return

    def getGeophysicalNoDataValue(self):
        return self.Band_getGeophysicalNoDataValue(self.__obj)

    def setGeophysicalNoDataValue(self, noDataValue):
        self.Band_setGeophysicalNoDataValue(self.__obj, noDataValue)
        return

    def getValidPixelExpression(self):
        return self.Band_getValidPixelExpression(self.__obj)

    def setValidPixelExpression(self, validPixelExpression):
        self.Band_setValidPixelExpression(self.__obj, validPixelExpression)
        return

    def isValidMaskUsed(self):
        return self.Band_isValidMaskUsed(self.__obj)

    def resetValidMask(self):
        self.Band_resetValidMask(self.__obj)
        return

    def getValidMaskExpression(self):
        return self.Band_getValidMaskExpression(self.__obj)

    def updateExpression(self, oldExternalName, newExternalName):
        self.Band_updateExpression(self.__obj, oldExternalName, newExternalName)
        return

    def hasRasterData(self):
        return self.Band_hasRasterData(self.__obj)

    def getRasterData(self):
        return ProductData(self.Band_getRasterData(self.__obj))

    def setRasterData(self, rasterData):
        self.Band_setRasterData(self.__obj, rasterData.__obj)
        return

    def loadRasterData(self):
        self.Band_loadRasterData(self.__obj)
        return

    def isPixelValid(self, x, y):
        return self.Band_isPixelValid(self.__obj, x, y)

    def getSampleInt(self, x, y):
        return self.Band_getSampleInt(self.__obj, x, y)

    def getSampleFloat(self, x, y):
        return self.Band_getSampleFloat(self.__obj, x, y)

    def getPixels(self, x, y, w, h, pixels):
        return self.Band_getPixelsInt(self.__obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        return self.Band_getPixelsFloat(self.__obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        return self.Band_getPixelsDouble(self.__obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return self.Band_readPixelsInt(self.__obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return self.Band_readPixelsFloat(self.__obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return self.Band_readPixelsDouble(self.__obj, x, y, w, h, pixels)

    def writePixels(self, x, y, w, h, pixels):
        self.Band_writePixelsInt(self.__obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        self.Band_writePixelsFloat(self.__obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        self.Band_writePixelsDouble(self.__obj, x, y, w, h, pixels)
        return

    def readValidMask(self, x, y, w, h, validMask):
        return self.Band_readValidMask(self.__obj, x, y, w, h, validMask)

    def readRasterDataFully(self):
        self.Band_readRasterDataFully(self.__obj)
        return

    def readRasterData(self, offsetX, offsetY, width, height, rasterData):
        self.Band_readRasterData(self.__obj, offsetX, offsetY, width, height, rasterData.__obj)
        return

    def writeRasterDataFully(self):
        self.Band_writeRasterDataFully(self.__obj)
        return

    def writeRasterData(self, offsetX, offsetY, width, height, rasterData):
        self.Band_writeRasterData(self.__obj, offsetX, offsetY, width, height, rasterData.__obj)
        return

    def createCompatibleRasterData(self):
        return ProductData(self.Band_createCompatibleRasterData(self.__obj))

    def createCompatibleSceneRasterData(self):
        return ProductData(self.Band_createCompatibleSceneRasterData(self.__obj))

    def createCompatibleRasterData(self, width, height):
        return ProductData(self.Band_createCompatibleRasterDataForRect(self.__obj, width, height))

    def isCompatibleRasterData(self, rasterData, w, h):
        return self.Band_isCompatibleRasterData(self.__obj, rasterData.__obj, w, h)

    def checkCompatibleRasterData(self, rasterData, w, h):
        self.Band_checkCompatibleRasterData(self.__obj, rasterData.__obj, w, h)
        return

    def hasIntPixels(self):
        return self.Band_hasIntPixels(self.__obj)

    def createTransectProfileData(self, shape):
        return TransectProfileData(self.Band_createTransectProfileData(self.__obj, shape.__obj))

    def getImageInfo(self):
        return ImageInfo(self.Band_getImageInfo(self.__obj))

    def setImageInfo(self, imageInfo):
        self.Band_setImageInfo(self.__obj, imageInfo.__obj)
        return

    def fireImageInfoChanged(self):
        self.Band_fireImageInfoChanged(self.__obj)
        return

    def createDefaultImageInfo(self, histoSkipAreas, histogram):
        return ImageInfo(self.Band_createDefaultImageInfo(self.__obj, histoSkipAreas, histogram.__obj))

    def getOverlayMaskGroup(self):
        return ProductNodeGroup(self.Band_getOverlayMaskGroup(self.__obj))

    def createColorIndexedImage(self, pm):
        return BufferedImage(self.Band_createColorIndexedImage(self.__obj, pm.__obj))

    def createRgbImage(self, pm):
        return BufferedImage(self.Band_createRgbImage(self.__obj, pm.__obj))

    def createPixelValidator(self, lineOffset, roi):
        return IndexValidator(self.Band_createPixelValidator(self.__obj, lineOffset, roi.__obj))

    def scale(self, v):
        return self.Band_scale(self.__obj, v)

    def scaleInverse(self, v):
        return self.Band_scaleInverse(self.__obj, v)

    def getPixelString(self, x, y):
        return self.Band_getPixelString(self.__obj, x, y)

    def isSourceImageSet(self):
        return self.Band_isSourceImageSet(self.__obj)

    def getSourceImage(self):
        return MultiLevelImage(self.Band_getSourceImage(self.__obj))

    def isGeophysicalImageSet(self):
        return self.Band_isGeophysicalImageSet(self.__obj)

    def getGeophysicalImage(self):
        return MultiLevelImage(self.Band_getGeophysicalImage(self.__obj))

    def isValidMaskImageSet(self):
        return self.Band_isValidMaskImageSet(self.__obj)

    def getValidMaskImage(self):
        return MultiLevelImage(self.Band_getValidMaskImage(self.__obj))

    def isStxSet(self):
        return self.Band_isStxSet(self.__obj)

    def getStx(self):
        return Stx(self.Band_getStx(self.__obj))

    def setStx(self, stx):
        self.Band_setStx(self.__obj, stx.__obj)
        return

    def getValidShape(self):
        return Shape(self.Band_getValidShape(self.__obj))

    def getDataType(self):
        return self.Band_getDataType(self.__obj)

    def getNumDataElems(self):
        return self.Band_getNumDataElems(self.__obj)

    def setData(self, data):
        self.Band_setData(self.__obj, data.__obj)
        return

    def getData(self):
        return ProductData(self.Band_getData(self.__obj))

    def setDataElems(self, elems):
        self.Band_setDataElems(self.__obj, elems.__obj)
        return

    def getDataElems(self):
        return Object(self.Band_getDataElems(self.__obj))

    def getDataElemSize(self):
        return self.Band_getDataElemSize(self.__obj)

    def setReadOnly(self, readOnly):
        self.Band_setReadOnly(self.__obj, readOnly)
        return

    def isReadOnly(self):
        return self.Band_isReadOnly(self.__obj)

    def setUnit(self, unit):
        self.Band_setUnit(self.__obj, unit)
        return

    def getUnit(self):
        return self.Band_getUnit(self.__obj)

    def fireProductNodeDataChanged(self):
        self.Band_fireProductNodeDataChanged(self.__obj)
        return

    def createCompatibleProductData(self, numElems):
        return ProductData(self.Band_createCompatibleProductData(self.__obj, numElems))

    def getOwner(self):
        return ProductNode(self.Band_getOwner(self.__obj))

    def getName(self):
        return self.Band_getName(self.__obj)

    def setName(self, name):
        self.Band_setName(self.__obj, name)
        return

    def getDescription(self):
        return self.Band_getDescription(self.__obj)

    def setDescription(self, description):
        self.Band_setDescription(self.__obj, description)
        return

    def isModified(self):
        return self.Band_isModified(self.__obj)

    def isValidNodeName(name):
        return Band_isValidNodeName(name)

    def getProduct(self):
        return Product(self.Band_getProduct(self.__obj))

    def getProductReader(self):
        return ProductReader(self.Band_getProductReader(self.__obj))

    def getProductWriter(self):
        return ProductWriter(self.Band_getProductWriter(self.__obj))

    def getDisplayName(self):
        return self.Band_getDisplayName(self.__obj)

    def getProductRefString(self):
        return self.Band_getProductRefString(self.__obj)


class ColorPaletteDef_Point:

    def __init__(self, obj):
        self.__obj = obj


class RenderedImage:

    def __init__(self, obj):
        self.__obj = obj


class Placemark:

    def __init__(self, obj):
        self.__obj = obj

    def newPlacemark(descriptor, feature):
        return Placemark(Placemark_newPlacemark(descriptor.__obj, feature.__obj))

    def createPointPlacemark(descriptor, name, label, text, pixelPos, geoPos, geoCoding):
        return Placemark(Placemark_createPointPlacemark(descriptor.__obj, name, label, text, pixelPos.__obj, geoPos.__obj, geoCoding.__obj))

    def getDescriptor(self):
        return PlacemarkDescriptor(self.Placemark_getDescriptor(self.__obj))

    def getFeature(self):
        return SimpleFeature(self.Placemark_getFeature(self.__obj))

    def getAttributeValue(self, attributeName):
        return Object(self.Placemark_getAttributeValue(self.__obj, attributeName))

    def setAttributeValue(self, attributeName, attributeValue):
        self.Placemark_setAttributeValue(self.__obj, attributeName, attributeValue.__obj)
        return

    def setLabel(self, label):
        self.Placemark_setLabel(self.__obj, label)
        return

    def getLabel(self):
        return self.Placemark_getLabel(self.__obj)

    def setText(self, text):
        self.Placemark_setText(self.__obj, text)
        return

    def getText(self):
        return self.Placemark_getText(self.__obj)

    def setStyleCss(self, styleCss):
        self.Placemark_setStyleCss(self.__obj, styleCss)
        return

    def getStyleCss(self):
        return self.Placemark_getStyleCss(self.__obj)

    def acceptVisitor(self, visitor):
        self.Placemark_acceptVisitor(self.__obj, visitor.__obj)
        return

    def getPixelPos(self):
        return PixelPos(self.Placemark_getPixelPos(self.__obj))

    def setPixelPos(self, pixelPos):
        self.Placemark_setPixelPos(self.__obj, pixelPos.__obj)
        return

    def getGeoPos(self):
        return GeoPos(self.Placemark_getGeoPos(self.__obj))

    def setGeoPos(self, geoPos):
        self.Placemark_setGeoPos(self.__obj, geoPos.__obj)
        return

    def updatePositions(self):
        self.Placemark_updatePositions(self.__obj)
        return

    def createPinFeatureType():
        return SimpleFeatureType(Placemark_createPinFeatureType())

    def createGcpFeatureType():
        return SimpleFeatureType(Placemark_createGcpFeatureType())

    def createGeometryFeatureType():
        return SimpleFeatureType(Placemark_createGeometryFeatureType())

    def createPointFeatureType(name):
        return SimpleFeatureType(Placemark_createPointFeatureType(name))

    def getOwner(self):
        return ProductNode(self.Placemark_getOwner(self.__obj))

    def getName(self):
        return self.Placemark_getName(self.__obj)

    def setName(self, name):
        self.Placemark_setName(self.__obj, name)
        return

    def getDescription(self):
        return self.Placemark_getDescription(self.__obj)

    def setDescription(self, description):
        self.Placemark_setDescription(self.__obj, description)
        return

    def isModified(self):
        return self.Placemark_isModified(self.__obj)

    def setModified(self, modified):
        self.Placemark_setModified(self.__obj, modified)
        return

    def toString(self):
        return self.Placemark_toString(self.__obj)

    def dispose(self):
        self.Placemark_dispose(self.__obj)
        return

    def isValidNodeName(name):
        return Placemark_isValidNodeName(name)

    def getProduct(self):
        return Product(self.Placemark_getProduct(self.__obj))

    def getProductReader(self):
        return ProductReader(self.Placemark_getProductReader(self.__obj))

    def getProductWriter(self):
        return ProductWriter(self.Placemark_getProductWriter(self.__obj))

    def getDisplayName(self):
        return self.Placemark_getDisplayName(self.__obj)

    def getProductRefString(self):
        return self.Placemark_getProductRefString(self.__obj)

    def updateExpression(self, oldExternalName, newExternalName):
        self.Placemark_updateExpression(self.__obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        self.Placemark_removeFromFile(self.__obj, productWriter.__obj)
        return


class IndexValidator:

    def __init__(self, obj):
        self.__obj = obj


class Area:

    def __init__(self, obj):
        self.__obj = obj


class ComponentColorModel:

    def __init__(self, obj):
        self.__obj = obj


class Iterator:

    def __init__(self, obj):
        self.__obj = obj


class MathTransform:

    def __init__(self, obj):
        self.__obj = obj


class CoordinateReferenceSystem:

    def __init__(self, obj):
        self.__obj = obj


class ProductWriterPlugIn:

    def __init__(self, obj):
        self.__obj = obj


class File:

    def __init__(self, obj):
        self.__obj = obj


class GeoPos:

    def __init__(self, obj):
        self.__obj = obj

    def newGeoPos(lat, lon):
        return GeoPos(GeoPos_newGeoPos(lat, lon))

    def getLat(self):
        return self.GeoPos_getLat(self.__obj)

    def getLon(self):
        return self.GeoPos_getLon(self.__obj)

    def setLocation(self, lat, lon):
        self.GeoPos_setLocation(self.__obj, lat, lon)
        return

    def isValid(self):
        return self.GeoPos_isValid(self.__obj)

    def areValid(a):
        return GeoPos_areValid(a.__obj)

    def setInvalid(self):
        self.GeoPos_setInvalid(self.__obj)
        return

    def equals(self, obj):
        return self.GeoPos_equals(self.__obj, obj.__obj)

    def hashCode(self):
        return self.GeoPos_hashCode(self.__obj)

    def toString(self):
        return self.GeoPos_toString(self.__obj)

    def normalize(self):
        self.GeoPos_normalize(self.__obj)
        return

    def normalizeLon(lon):
        return GeoPos_normalizeLon(lon)

    def getLatString(self):
        return self.GeoPos_getLatString(self.__obj)

    def getLonString(self):
        return self.GeoPos_getLonString(self.__obj)


class ProductNodeGroup:

    def __init__(self, obj):
        self.__obj = obj

    def newProductNodeGroup(name):
        return ProductNodeGroup(ProductNodeGroup_newProductNodeGroup1(name))

    def newProductNodeGroup(owner, name, takingOverNodeOwnership):
        return ProductNodeGroup(ProductNodeGroup_newProductNodeGroup2(owner.__obj, name, takingOverNodeOwnership))

    def isTakingOverNodeOwnership(self):
        return self.ProductNodeGroup_isTakingOverNodeOwnership(self.__obj)

    def getNodeCount(self):
        return self.ProductNodeGroup_getNodeCount(self.__obj)

    def get(self, index):
        return T(self.ProductNodeGroup_get1(self.__obj, index))

    def getNodeDisplayNames(self):
        return self.ProductNodeGroup_getNodeDisplayNames(self.__obj)

    def getNodeNames(self):
        return self.ProductNodeGroup_getNodeNames(self.__obj)

    def toArray(self):
        return ProductNode(self.ProductNodeGroup_toArray1(self.__obj))

    def toArray(self, array):
        return T(self.ProductNodeGroup_toArray2(self.__obj, array.__obj))

    def indexOf(self, name):
        return self.ProductNodeGroup_indexOf2(self.__obj, name)

    def indexOf(self, element):
        return self.ProductNodeGroup_indexOf1(self.__obj, element.__obj)

    def getByDisplayName(self, displayName):
        return T(self.ProductNodeGroup_getByDisplayName(self.__obj, displayName))

    def get(self, name):
        return T(self.ProductNodeGroup_get2(self.__obj, name))

    def contains(self, name):
        return self.ProductNodeGroup_contains2(self.__obj, name)

    def contains(self, node):
        return self.ProductNodeGroup_contains1(self.__obj, node.__obj)

    def add(self, node):
        return self.ProductNodeGroup_add2(self.__obj, node.__obj)

    def add(self, index, node):
        self.ProductNodeGroup_add1(self.__obj, index, node.__obj)
        return

    def remove(self, node):
        return self.ProductNodeGroup_remove(self.__obj, node.__obj)

    def removeAll(self):
        self.ProductNodeGroup_removeAll(self.__obj)
        return

    def clearRemovedList(self):
        self.ProductNodeGroup_clearRemovedList(self.__obj)
        return

    def getRemovedNodes(self):
        return Collection(self.ProductNodeGroup_getRemovedNodes(self.__obj))

    def getRawStorageSize(self, subsetDef):
        return self.ProductNodeGroup_getRawStorageSize2(self.__obj, subsetDef.__obj)

    def setModified(self, modified):
        self.ProductNodeGroup_setModified(self.__obj, modified)
        return

    def acceptVisitor(self, visitor):
        self.ProductNodeGroup_acceptVisitor(self.__obj, visitor.__obj)
        return

    def dispose(self):
        self.ProductNodeGroup_dispose(self.__obj)
        return

    def updateExpression(self, oldExternalName, newExternalName):
        self.ProductNodeGroup_updateExpression(self.__obj, oldExternalName, newExternalName)
        return

    def getOwner(self):
        return ProductNode(self.ProductNodeGroup_getOwner(self.__obj))

    def getName(self):
        return self.ProductNodeGroup_getName(self.__obj)

    def setName(self, name):
        self.ProductNodeGroup_setName(self.__obj, name)
        return

    def getDescription(self):
        return self.ProductNodeGroup_getDescription(self.__obj)

    def setDescription(self, description):
        self.ProductNodeGroup_setDescription(self.__obj, description)
        return

    def isModified(self):
        return self.ProductNodeGroup_isModified(self.__obj)

    def toString(self):
        return self.ProductNodeGroup_toString(self.__obj)

    def isValidNodeName(name):
        return ProductNodeGroup_isValidNodeName(name)

    def getProduct(self):
        return Product(self.ProductNodeGroup_getProduct(self.__obj))

    def getProductReader(self):
        return ProductReader(self.ProductNodeGroup_getProductReader(self.__obj))

    def getProductWriter(self):
        return ProductWriter(self.ProductNodeGroup_getProductWriter(self.__obj))

    def getDisplayName(self):
        return self.ProductNodeGroup_getDisplayName(self.__obj)

    def getProductRefString(self):
        return self.ProductNodeGroup_getProductRefString(self.__obj)

    def getRawStorageSize(self):
        return self.ProductNodeGroup_getRawStorageSize1(self.__obj)

    def fireProductNodeChanged(self, propertyName):
        self.ProductNodeGroup_fireProductNodeChanged1(self.__obj, propertyName)
        return

    def fireProductNodeChanged(self, propertyName, oldValue, newValue):
        self.ProductNodeGroup_fireProductNodeChanged2(self.__obj, propertyName, oldValue.__obj, newValue.__obj)
        return

    def removeFromFile(self, productWriter):
        self.ProductNodeGroup_removeFromFile(self.__obj, productWriter.__obj)
        return


class MapProjection:

    def __init__(self, obj):
        self.__obj = obj


class ProductManager:

    def __init__(self, obj):
        self.__obj = obj

    def newProductManager():
        return ProductManager(ProductManager_newProductManager())

    def getProductCount(self):
        return self.ProductManager_getProductCount(self.__obj)

    def getProduct(self, index):
        return Product(self.ProductManager_getProduct(self.__obj, index))

    def getProductDisplayNames(self):
        return self.ProductManager_getProductDisplayNames(self.__obj)

    def getProductNames(self):
        return self.ProductManager_getProductNames(self.__obj)

    def getProducts(self):
        return Product(self.ProductManager_getProducts(self.__obj))

    def getProductByDisplayName(self, displayName):
        return Product(self.ProductManager_getProductByDisplayName(self.__obj, displayName))

    def getProductByRefNo(self, refNo):
        return Product(self.ProductManager_getProductByRefNo(self.__obj, refNo))

    def getProduct(self, name):
        return Product(self.ProductManager_getProductByName(self.__obj, name))

    def getProductIndex(self, product):
        return self.ProductManager_getProductIndex(self.__obj, product.__obj)

    def containsProduct(self, name):
        return self.ProductManager_containsProduct(self.__obj, name)

    def contains(self, product):
        return self.ProductManager_contains(self.__obj, product.__obj)

    def addProduct(self, product):
        self.ProductManager_addProduct(self.__obj, product.__obj)
        return

    def removeProduct(self, product):
        return self.ProductManager_removeProduct(self.__obj, product.__obj)

    def removeAllProducts(self):
        self.ProductManager_removeAllProducts(self.__obj)
        return

    def addListener(self, listener):
        return self.ProductManager_addListener(self.__obj, listener.__obj)

    def removeListener(self, listener):
        return self.ProductManager_removeListener(self.__obj, listener.__obj)


class FlagCoding:

    def __init__(self, obj):
        self.__obj = obj

    def newFlagCoding(name):
        return FlagCoding(FlagCoding_newFlagCoding(name))

    def getFlag(self, name):
        return MetadataAttribute(self.FlagCoding_getFlag(self.__obj, name))

    def getFlagNames(self):
        return self.FlagCoding_getFlagNames(self.__obj)

    def addFlag(self, name, flagMask, description):
        return MetadataAttribute(self.FlagCoding_addFlag(self.__obj, name, flagMask, description))

    def getFlagMask(self, name):
        return self.FlagCoding_getFlagMask(self.__obj, name)

    def acceptVisitor(self, visitor):
        self.FlagCoding_acceptVisitor(self.__obj, visitor.__obj)
        return

    def addElement(self, element):
        self.FlagCoding_addElement(self.__obj, element.__obj)
        return

    def addAttribute(self, attribute):
        self.FlagCoding_addAttribute(self.__obj, attribute.__obj)
        return

    def addSample(self, name, value, description):
        return MetadataAttribute(self.FlagCoding_addSample(self.__obj, name, value, description))

    def getSampleCount(self):
        return self.FlagCoding_getSampleCount(self.__obj)

    def getSampleName(self, index):
        return self.FlagCoding_getSampleName(self.__obj, index)

    def getSampleValue(self, index):
        return self.FlagCoding_getSampleValue(self.__obj, index)

    def getElementGroup(self):
        return ProductNodeGroup(self.FlagCoding_getElementGroup(self.__obj))

    def getParentElement(self):
        return MetadataElement(self.FlagCoding_getParentElement(self.__obj))

    def addElementAt(self, element, index):
        self.FlagCoding_addElementAt(self.__obj, element.__obj, index)
        return

    def removeElement(self, element):
        return self.FlagCoding_removeElement(self.__obj, element.__obj)

    def getNumElements(self):
        return self.FlagCoding_getNumElements(self.__obj)

    def getElementAt(self, index):
        return MetadataElement(self.FlagCoding_getElementAt(self.__obj, index))

    def getElementNames(self):
        return self.FlagCoding_getElementNames(self.__obj)

    def getElements(self):
        return MetadataElement(self.FlagCoding_getElements(self.__obj))

    def getElement(self, name):
        return MetadataElement(self.FlagCoding_getElement(self.__obj, name))

    def containsElement(self, name):
        return self.FlagCoding_containsElement(self.__obj, name)

    def getElementIndex(self, element):
        return self.FlagCoding_getElementIndex(self.__obj, element.__obj)

    def removeAttribute(self, attribute):
        return self.FlagCoding_removeAttribute(self.__obj, attribute.__obj)

    def getNumAttributes(self):
        return self.FlagCoding_getNumAttributes(self.__obj)

    def getAttributeAt(self, index):
        return MetadataAttribute(self.FlagCoding_getAttributeAt(self.__obj, index))

    def getAttributeNames(self):
        return self.FlagCoding_getAttributeNames(self.__obj)

    def getAttributes(self):
        return MetadataAttribute(self.FlagCoding_getAttributes(self.__obj))

    def getAttribute(self, name):
        return MetadataAttribute(self.FlagCoding_getAttribute(self.__obj, name))

    def containsAttribute(self, name):
        return self.FlagCoding_containsAttribute(self.__obj, name)

    def getAttributeIndex(self, attribute):
        return self.FlagCoding_getAttributeIndex(self.__obj, attribute.__obj)

    def getAttributeDouble(self, name, defaultValue):
        return self.FlagCoding_getAttributeDouble(self.__obj, name, defaultValue)

    def getAttributeUTC(self, name, defaultValue):
        return ProductData_UTC(self.FlagCoding_getAttributeUTC(self.__obj, name, defaultValue.__obj))

    def getAttributeInt(self, name, defaultValue):
        return self.FlagCoding_getAttributeInt(self.__obj, name, defaultValue)

    def setAttributeInt(self, name, value):
        self.FlagCoding_setAttributeInt(self.__obj, name, value)
        return

    def setAttributeDouble(self, name, value):
        self.FlagCoding_setAttributeDouble(self.__obj, name, value)
        return

    def setAttributeUTC(self, name, value):
        self.FlagCoding_setAttributeUTC(self.__obj, name, value.__obj)
        return

    def getAttributeString(self, name, defaultValue):
        return self.FlagCoding_getAttributeString(self.__obj, name, defaultValue)

    def setAttributeString(self, name, value):
        self.FlagCoding_setAttributeString(self.__obj, name, value)
        return

    def setModified(self, modified):
        self.FlagCoding_setModified(self.__obj, modified)
        return

    def createDeepClone(self):
        return MetadataElement(self.FlagCoding_createDeepClone(self.__obj))

    def dispose(self):
        self.FlagCoding_dispose(self.__obj)
        return

    def getOwner(self):
        return ProductNode(self.FlagCoding_getOwner(self.__obj))

    def getName(self):
        return self.FlagCoding_getName(self.__obj)

    def setName(self, name):
        self.FlagCoding_setName(self.__obj, name)
        return

    def getDescription(self):
        return self.FlagCoding_getDescription(self.__obj)

    def setDescription(self, description):
        self.FlagCoding_setDescription(self.__obj, description)
        return

    def isModified(self):
        return self.FlagCoding_isModified(self.__obj)

    def toString(self):
        return self.FlagCoding_toString(self.__obj)

    def isValidNodeName(name):
        return FlagCoding_isValidNodeName(name)

    def getProduct(self):
        return Product(self.FlagCoding_getProduct(self.__obj))

    def getProductReader(self):
        return ProductReader(self.FlagCoding_getProductReader(self.__obj))

    def getProductWriter(self):
        return ProductWriter(self.FlagCoding_getProductWriter(self.__obj))

    def getDisplayName(self):
        return self.FlagCoding_getDisplayName(self.__obj)

    def getProductRefString(self):
        return self.FlagCoding_getProductRefString(self.__obj)

    def updateExpression(self, oldExternalName, newExternalName):
        self.FlagCoding_updateExpression(self.__obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        self.FlagCoding_removeFromFile(self.__obj, productWriter.__obj)
        return


class IndexColorModel:

    def __init__(self, obj):
        self.__obj = obj


class ProductNodeListener:

    def __init__(self, obj):
        self.__obj = obj


class ImageInfo_HistogramMatching:

    def __init__(self, obj):
        self.__obj = obj


class Map:

    def __init__(self, obj):
        self.__obj = obj


class ProductUtils:

    def __init__(self, obj):
        self.__obj = obj

    def newProductUtils():
        return ProductUtils(ProductUtils_newProductUtils())

    def createImageInfo(rasters, assignMissingImageInfos, pm):
        return ImageInfo(ProductUtils_createImageInfo(rasters.__obj, assignMissingImageInfos, pm.__obj))

    def createRgbImage(rasters, imageInfo, pm):
        return BufferedImage(ProductUtils_createRgbImage(rasters.__obj, imageInfo.__obj, pm.__obj))

    def createColorIndexedImage(rasterDataNode, pm):
        return BufferedImage(ProductUtils_createColorIndexedImage(rasterDataNode.__obj, pm.__obj))

    def createSuitableMapInfo(product, rect, mapProjection):
        return MapInfo(ProductUtils_createSuitableMapInfo1(product.__obj, rect.__obj, mapProjection.__obj))

    def createSuitableMapInfo(product, mapProjection, orientation, noDataValue):
        return MapInfo(ProductUtils_createSuitableMapInfo2(product.__obj, mapProjection.__obj, orientation, noDataValue))

    def getOutputRasterSize(product, rect, mapTransform, pixelSizeX, pixelSizeY):
        return Dimension(ProductUtils_getOutputRasterSize(product.__obj, rect.__obj, mapTransform.__obj, pixelSizeX, pixelSizeY))

    def createMapEnvelope(product, rect, mapTransform):
        return Point2D(ProductUtils_createMapEnvelope2(product.__obj, rect.__obj, mapTransform.__obj))

    def createMapEnvelope(product, rect, step, mapTransform):
        return Point2D(ProductUtils_createMapEnvelope1(product.__obj, rect.__obj, step, mapTransform.__obj))

    def getMinMax(boundary):
        return Point2D(ProductUtils_getMinMax(boundary.__obj))

    def createMapBoundary(product, rect, step, mapTransform):
        return Point2D(ProductUtils_createMapBoundary(product.__obj, rect.__obj, step, mapTransform.__obj))

    def createGeoBoundary(product, step):
        return GeoPos(ProductUtils_createGeoBoundary1(product.__obj, step))

    def createGeoBoundary(product, region, step):
        return GeoPos(ProductUtils_createGeoBoundary2(product.__obj, region.__obj, step))

    def createGeoBoundary(product, region, step, usePixelCenter):
        return GeoPos(ProductUtils_createGeoBoundary3(product.__obj, region.__obj, step, usePixelCenter))

    def createGeoBoundary(raster, region, step):
        return GeoPos(ProductUtils_createGeoBoundary4(raster.__obj, region.__obj, step))

    def createGeoBoundaryPaths(product):
        return GeneralPath(ProductUtils_createGeoBoundaryPaths1(product.__obj))

    def createGeoBoundaryPaths(product, region, step):
        return GeneralPath(ProductUtils_createGeoBoundaryPaths2(product.__obj, region.__obj, step))

    def createGeoBoundaryPaths(product, region, step, usePixelCenter):
        return GeneralPath(ProductUtils_createGeoBoundaryPaths3(product.__obj, region.__obj, step, usePixelCenter))

    def createPixelBoundary(product, rect, step):
        return PixelPos(ProductUtils_createPixelBoundary1(product.__obj, rect.__obj, step))

    def createPixelBoundary(product, rect, step, usePixelCenter):
        return PixelPos(ProductUtils_createPixelBoundary2(product.__obj, rect.__obj, step, usePixelCenter))

    def createPixelBoundary(raster, rect, step):
        return PixelPos(ProductUtils_createPixelBoundary3(raster.__obj, rect.__obj, step))

    def createRectBoundary(rect, step):
        return PixelPos(ProductUtils_createRectBoundary1(rect.__obj, step))

    def createRectBoundary(rect, step, usePixelCenter):
        return PixelPos(ProductUtils_createRectBoundary2(rect.__obj, step, usePixelCenter))

    def copyFlagCodings(source, target):
        ProductUtils_copyFlagCodings(source.__obj, target.__obj)
        return

    def copyFlagCoding(sourceFlagCoding, target):
        return FlagCoding(ProductUtils_copyFlagCoding(sourceFlagCoding.__obj, target.__obj))

    def copyIndexCoding(sourceIndexCoding, target):
        return IndexCoding(ProductUtils_copyIndexCoding(sourceIndexCoding.__obj, target.__obj))

    def copyMasks(sourceProduct, targetProduct):
        ProductUtils_copyMasks(sourceProduct.__obj, targetProduct.__obj)
        return

    def copyOverlayMasks(sourceProduct, targetProduct):
        ProductUtils_copyOverlayMasks(sourceProduct.__obj, targetProduct.__obj)
        return

    def copyFlagBands(sourceProduct, targetProduct, copySourceImage):
        ProductUtils_copyFlagBands(sourceProduct.__obj, targetProduct.__obj, copySourceImage)
        return

    def copyTiePointGrid(gridName, sourceProduct, targetProduct):
        return TiePointGrid(ProductUtils_copyTiePointGrid(gridName, sourceProduct.__obj, targetProduct.__obj))

    def copyBand(sourceBandName, sourceProduct, targetProduct, copySourceImage):
        return Band(ProductUtils_copyBand2(sourceBandName, sourceProduct.__obj, targetProduct.__obj, copySourceImage))

    def copyBand(sourceBandName, sourceProduct, targetBandName, targetProduct, copySourceImage):
        return Band(ProductUtils_copyBand1(sourceBandName, sourceProduct.__obj, targetBandName, targetProduct.__obj, copySourceImage))

    def copyRasterDataNodeProperties(sourceRaster, targetRaster):
        ProductUtils_copyRasterDataNodeProperties(sourceRaster.__obj, targetRaster.__obj)
        return

    def copySpectralBandProperties(sourceBand, targetBand):
        ProductUtils_copySpectralBandProperties(sourceBand.__obj, targetBand.__obj)
        return

    def copyGeoCoding(sourceProduct, targetProduct):
        ProductUtils_copyGeoCoding(sourceProduct.__obj, targetProduct.__obj)
        return

    def copyTiePointGrids(sourceProduct, targetProduct):
        ProductUtils_copyTiePointGrids(sourceProduct.__obj, targetProduct.__obj)
        return

    def copyVectorData(sourceProduct, targetProduct):
        ProductUtils_copyVectorData(sourceProduct.__obj, targetProduct.__obj)
        return

    def canGetPixelPos(product):
        return ProductUtils_canGetPixelPos1(product.__obj)

    def canGetPixelPos(raster):
        return ProductUtils_canGetPixelPos2(raster.__obj)

    def createDensityPlotImage(raster1, sampleMin1, sampleMax1, raster2, sampleMin2, sampleMax2, roiMask, width, height, background, image, pm):
        return BufferedImage(ProductUtils_createDensityPlotImage(raster1.__obj, sampleMin1, sampleMax1, raster2.__obj, sampleMin2, sampleMax2, roiMask.__obj, width, height, background.__obj, image.__obj, pm.__obj))

    def overlayMasks(raster, overlayBIm, pm):
        return BufferedImage(ProductUtils_overlayMasks(raster.__obj, overlayBIm.__obj, pm.__obj))

    def getCenterGeoPos(product):
        return GeoPos(ProductUtils_getCenterGeoPos(product.__obj))

    def normalizeGeoPolygon(polygon):
        return ProductUtils_normalizeGeoPolygon(polygon.__obj)

    def normalizeGeoPolygon_old(polygon):
        return ProductUtils_normalizeGeoPolygon_old(polygon.__obj)

    def denormalizeGeoPolygon(polygon):
        ProductUtils_denormalizeGeoPolygon(polygon.__obj)
        return

    def denormalizeGeoPos(geoPos):
        ProductUtils_denormalizeGeoPos(geoPos.__obj)
        return

    def denormalizeGeoPos_old(geoPos):
        ProductUtils_denormalizeGeoPos_old(geoPos.__obj)
        return

    def getRotationDirection(polygon):
        return ProductUtils_getRotationDirection(polygon.__obj)

    def getAngleSum(polygon):
        return ProductUtils_getAngleSum(polygon.__obj)

    def convertToPixelPath(geoPath, geoCoding):
        return GeneralPath(ProductUtils_convertToPixelPath(geoPath.__obj, geoCoding.__obj))

    def convertToGeoPath(shape, geoCoding):
        return GeneralPath(ProductUtils_convertToGeoPath(shape.__obj, geoCoding.__obj))

    def copyMetadata(source, target):
        ProductUtils_copyMetadata2(source.__obj, target.__obj)
        return

    def copyMetadata(source, target):
        ProductUtils_copyMetadata1(source.__obj, target.__obj)
        return

    def copyPreferredTileSize(sourceProduct, targetProduct):
        ProductUtils_copyPreferredTileSize(sourceProduct.__obj, targetProduct.__obj)
        return

    def createGeoTIFFMetadata(product):
        return GeoTIFFMetadata(ProductUtils_createGeoTIFFMetadata2(product.__obj))

    def createGeoTIFFMetadata(geoCoding, width, height):
        return GeoTIFFMetadata(ProductUtils_createGeoTIFFMetadata1(geoCoding.__obj, width, height))

    def areaToPath(negativeArea, deltaX):
        return GeneralPath(ProductUtils_areaToPath(negativeArea.__obj, deltaX))

    def addElementToHistory(product, elem):
        ProductUtils_addElementToHistory(product.__obj, elem.__obj)
        return

    def removeInvalidExpressions(product):
        return ProductUtils_removeInvalidExpressions(product.__obj)

    def findSuitableQuicklookBandName(product):
        return ProductUtils_findSuitableQuicklookBandName(product.__obj)

    def computeSourcePixelCoordinates(sourceGeoCoding, sourceWidth, sourceHeight, destGeoCoding, destArea):
        return PixelPos(ProductUtils_computeSourcePixelCoordinates(sourceGeoCoding.__obj, sourceWidth, sourceHeight, destGeoCoding.__obj, destArea.__obj))

    def computeMinMaxY(pixelPositions):
        return ProductUtils_computeMinMaxY(pixelPositions.__obj)

    def copyBandsForGeomTransform(sourceProduct, targetProduct, defaultNoDataValue, addedRasterDataNodes):
        ProductUtils_copyBandsForGeomTransform1(sourceProduct.__obj, targetProduct.__obj, defaultNoDataValue, addedRasterDataNodes.__obj)
        return

    def copyBandsForGeomTransform(sourceProduct, targetProduct, includeTiePointGrids, defaultNoDataValue, targetToSourceMap):
        ProductUtils_copyBandsForGeomTransform2(sourceProduct.__obj, targetProduct.__obj, includeTiePointGrids, defaultNoDataValue, targetToSourceMap.__obj)
        return

    def getScanLineTime(product, y):
        return ProductData_UTC(ProductUtils_getScanLineTime(product.__obj, y))

    def getGeophysicalSampleDouble(band, pixelX, pixelY, level):
        return ProductUtils_getGeophysicalSampleDouble(band.__obj, pixelX, pixelY, level)

    def getGeophysicalSampleLong(band, pixelX, pixelY, level):
        return ProductUtils_getGeophysicalSampleLong(band.__obj, pixelX, pixelY, level)


class MetadataElement:

    def __init__(self, obj):
        self.__obj = obj

    def newMetadataElement(name):
        return MetadataElement(MetadataElement_newMetadataElement(name))

    def getElementGroup(self):
        return ProductNodeGroup(self.MetadataElement_getElementGroup(self.__obj))

    def getParentElement(self):
        return MetadataElement(self.MetadataElement_getParentElement(self.__obj))

    def addElement(self, element):
        self.MetadataElement_addElement(self.__obj, element.__obj)
        return

    def addElementAt(self, element, index):
        self.MetadataElement_addElementAt(self.__obj, element.__obj, index)
        return

    def removeElement(self, element):
        return self.MetadataElement_removeElement(self.__obj, element.__obj)

    def getNumElements(self):
        return self.MetadataElement_getNumElements(self.__obj)

    def getElementAt(self, index):
        return MetadataElement(self.MetadataElement_getElementAt(self.__obj, index))

    def getElementNames(self):
        return self.MetadataElement_getElementNames(self.__obj)

    def getElements(self):
        return MetadataElement(self.MetadataElement_getElements(self.__obj))

    def getElement(self, name):
        return MetadataElement(self.MetadataElement_getElement(self.__obj, name))

    def containsElement(self, name):
        return self.MetadataElement_containsElement(self.__obj, name)

    def getElementIndex(self, element):
        return self.MetadataElement_getElementIndex(self.__obj, element.__obj)

    def addAttribute(self, attribute):
        self.MetadataElement_addAttribute(self.__obj, attribute.__obj)
        return

    def removeAttribute(self, attribute):
        return self.MetadataElement_removeAttribute(self.__obj, attribute.__obj)

    def getNumAttributes(self):
        return self.MetadataElement_getNumAttributes(self.__obj)

    def getAttributeAt(self, index):
        return MetadataAttribute(self.MetadataElement_getAttributeAt(self.__obj, index))

    def getAttributeNames(self):
        return self.MetadataElement_getAttributeNames(self.__obj)

    def getAttributes(self):
        return MetadataAttribute(self.MetadataElement_getAttributes(self.__obj))

    def getAttribute(self, name):
        return MetadataAttribute(self.MetadataElement_getAttribute(self.__obj, name))

    def containsAttribute(self, name):
        return self.MetadataElement_containsAttribute(self.__obj, name)

    def getAttributeIndex(self, attribute):
        return self.MetadataElement_getAttributeIndex(self.__obj, attribute.__obj)

    def getAttributeDouble(self, name, defaultValue):
        return self.MetadataElement_getAttributeDouble(self.__obj, name, defaultValue)

    def getAttributeUTC(self, name, defaultValue):
        return ProductData_UTC(self.MetadataElement_getAttributeUTC(self.__obj, name, defaultValue.__obj))

    def getAttributeInt(self, name, defaultValue):
        return self.MetadataElement_getAttributeInt(self.__obj, name, defaultValue)

    def setAttributeInt(self, name, value):
        self.MetadataElement_setAttributeInt(self.__obj, name, value)
        return

    def setAttributeDouble(self, name, value):
        self.MetadataElement_setAttributeDouble(self.__obj, name, value)
        return

    def setAttributeUTC(self, name, value):
        self.MetadataElement_setAttributeUTC(self.__obj, name, value.__obj)
        return

    def getAttributeString(self, name, defaultValue):
        return self.MetadataElement_getAttributeString(self.__obj, name, defaultValue)

    def setAttributeString(self, name, value):
        self.MetadataElement_setAttributeString(self.__obj, name, value)
        return

    def setModified(self, modified):
        self.MetadataElement_setModified(self.__obj, modified)
        return

    def acceptVisitor(self, visitor):
        self.MetadataElement_acceptVisitor(self.__obj, visitor.__obj)
        return

    def createDeepClone(self):
        return MetadataElement(self.MetadataElement_createDeepClone(self.__obj))

    def dispose(self):
        self.MetadataElement_dispose(self.__obj)
        return

    def getOwner(self):
        return ProductNode(self.MetadataElement_getOwner(self.__obj))

    def getName(self):
        return self.MetadataElement_getName(self.__obj)

    def setName(self, name):
        self.MetadataElement_setName(self.__obj, name)
        return

    def getDescription(self):
        return self.MetadataElement_getDescription(self.__obj)

    def setDescription(self, description):
        self.MetadataElement_setDescription(self.__obj, description)
        return

    def isModified(self):
        return self.MetadataElement_isModified(self.__obj)

    def toString(self):
        return self.MetadataElement_toString(self.__obj)

    def isValidNodeName(name):
        return MetadataElement_isValidNodeName(name)

    def getProduct(self):
        return Product(self.MetadataElement_getProduct(self.__obj))

    def getProductReader(self):
        return ProductReader(self.MetadataElement_getProductReader(self.__obj))

    def getProductWriter(self):
        return ProductWriter(self.MetadataElement_getProductWriter(self.__obj))

    def getDisplayName(self):
        return self.MetadataElement_getDisplayName(self.__obj)

    def getProductRefString(self):
        return self.MetadataElement_getProductRefString(self.__obj)

    def updateExpression(self, oldExternalName, newExternalName):
        self.MetadataElement_updateExpression(self.__obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        self.MetadataElement_removeFromFile(self.__obj, productWriter.__obj)
        return


class Pointing:

    def __init__(self, obj):
        self.__obj = obj


class Color:

    def __init__(self, obj):
        self.__obj = obj


class PlacemarkDescriptor:

    def __init__(self, obj):
        self.__obj = obj


class PointingFactory:

    def __init__(self, obj):
        self.__obj = obj


class TransectProfileData:

    def __init__(self, obj):
        self.__obj = obj


class PlacemarkGroup:

    def __init__(self, obj):
        self.__obj = obj

    def getVectorDataNode(self):
        return VectorDataNode(self.PlacemarkGroup_getVectorDataNode(self.__obj))

    def getPlacemark(self, feature):
        return Placemark(self.PlacemarkGroup_getPlacemark(self.__obj, feature.__obj))

    def add(self, placemark):
        return self.PlacemarkGroup_add4(self.__obj, placemark.__obj)

    def add(self, index, placemark):
        self.PlacemarkGroup_add2(self.__obj, index, placemark.__obj)
        return

    def remove(self, placemark):
        return self.PlacemarkGroup_remove2(self.__obj, placemark.__obj)

    def dispose(self):
        self.PlacemarkGroup_dispose(self.__obj)
        return

    def isTakingOverNodeOwnership(self):
        return self.PlacemarkGroup_isTakingOverNodeOwnership(self.__obj)

    def getNodeCount(self):
        return self.PlacemarkGroup_getNodeCount(self.__obj)

    def get(self, index):
        return T(self.PlacemarkGroup_get1(self.__obj, index))

    def getNodeDisplayNames(self):
        return self.PlacemarkGroup_getNodeDisplayNames(self.__obj)

    def getNodeNames(self):
        return self.PlacemarkGroup_getNodeNames(self.__obj)

    def toArray(self):
        return ProductNode(self.PlacemarkGroup_toArray1(self.__obj))

    def toArray(self, array):
        return T(self.PlacemarkGroup_toArray2(self.__obj, array.__obj))

    def indexOf(self, name):
        return self.PlacemarkGroup_indexOf2(self.__obj, name)

    def indexOf(self, element):
        return self.PlacemarkGroup_indexOf1(self.__obj, element.__obj)

    def getByDisplayName(self, displayName):
        return T(self.PlacemarkGroup_getByDisplayName(self.__obj, displayName))

    def get(self, name):
        return T(self.PlacemarkGroup_get2(self.__obj, name))

    def contains(self, name):
        return self.PlacemarkGroup_contains2(self.__obj, name)

    def contains(self, node):
        return self.PlacemarkGroup_contains1(self.__obj, node.__obj)

    def add(self, node):
        return self.PlacemarkGroup_add3(self.__obj, node.__obj)

    def add(self, index, node):
        self.PlacemarkGroup_add1(self.__obj, index, node.__obj)
        return

    def remove(self, node):
        return self.PlacemarkGroup_remove1(self.__obj, node.__obj)

    def removeAll(self):
        self.PlacemarkGroup_removeAll(self.__obj)
        return

    def clearRemovedList(self):
        self.PlacemarkGroup_clearRemovedList(self.__obj)
        return

    def getRemovedNodes(self):
        return Collection(self.PlacemarkGroup_getRemovedNodes(self.__obj))

    def getRawStorageSize(self, subsetDef):
        return self.PlacemarkGroup_getRawStorageSize2(self.__obj, subsetDef.__obj)

    def setModified(self, modified):
        self.PlacemarkGroup_setModified(self.__obj, modified)
        return

    def acceptVisitor(self, visitor):
        self.PlacemarkGroup_acceptVisitor(self.__obj, visitor.__obj)
        return

    def updateExpression(self, oldExternalName, newExternalName):
        self.PlacemarkGroup_updateExpression(self.__obj, oldExternalName, newExternalName)
        return

    def getOwner(self):
        return ProductNode(self.PlacemarkGroup_getOwner(self.__obj))

    def getName(self):
        return self.PlacemarkGroup_getName(self.__obj)

    def setName(self, name):
        self.PlacemarkGroup_setName(self.__obj, name)
        return

    def getDescription(self):
        return self.PlacemarkGroup_getDescription(self.__obj)

    def setDescription(self, description):
        self.PlacemarkGroup_setDescription(self.__obj, description)
        return

    def isModified(self):
        return self.PlacemarkGroup_isModified(self.__obj)

    def toString(self):
        return self.PlacemarkGroup_toString(self.__obj)

    def isValidNodeName(name):
        return PlacemarkGroup_isValidNodeName(name)

    def getProduct(self):
        return Product(self.PlacemarkGroup_getProduct(self.__obj))

    def getProductReader(self):
        return ProductReader(self.PlacemarkGroup_getProductReader(self.__obj))

    def getProductWriter(self):
        return ProductWriter(self.PlacemarkGroup_getProductWriter(self.__obj))

    def getDisplayName(self):
        return self.PlacemarkGroup_getDisplayName(self.__obj)

    def getProductRefString(self):
        return self.PlacemarkGroup_getProductRefString(self.__obj)

    def getRawStorageSize(self):
        return self.PlacemarkGroup_getRawStorageSize1(self.__obj)

    def fireProductNodeChanged(self, propertyName):
        self.PlacemarkGroup_fireProductNodeChanged1(self.__obj, propertyName)
        return

    def fireProductNodeChanged(self, propertyName, oldValue, newValue):
        self.PlacemarkGroup_fireProductNodeChanged2(self.__obj, propertyName, oldValue.__obj, newValue.__obj)
        return

    def removeFromFile(self, productWriter):
        self.PlacemarkGroup_removeFromFile(self.__obj, productWriter.__obj)
        return


class Product:

    def __init__(self, obj):
        self.__obj = obj

    def newProduct(name, type, sceneRasterWidth, sceneRasterHeight):
        return Product(Product_newProduct(name, type, sceneRasterWidth, sceneRasterHeight))

    def getFileLocation(self):
        return File(self.Product_getFileLocation(self.__obj))

    def setFileLocation(self, fileLocation):
        self.Product_setFileLocation(self.__obj, fileLocation.__obj)
        return

    def getProductType(self):
        return self.Product_getProductType(self.__obj)

    def setProductType(self, productType):
        self.Product_setProductType(self.__obj, productType)
        return

    def setProductReader(self, reader):
        self.Product_setProductReader(self.__obj, reader.__obj)
        return

    def getProductReader(self):
        return ProductReader(self.Product_getProductReader(self.__obj))

    def setProductWriter(self, writer):
        self.Product_setProductWriter(self.__obj, writer.__obj)
        return

    def getProductWriter(self):
        return ProductWriter(self.Product_getProductWriter(self.__obj))

    def writeHeader(self, output):
        self.Product_writeHeader(self.__obj, output.__obj)
        return

    def closeProductReader(self):
        self.Product_closeProductReader(self.__obj)
        return

    def closeProductWriter(self):
        self.Product_closeProductWriter(self.__obj)
        return

    def closeIO(self):
        self.Product_closeIO(self.__obj)
        return

    def dispose(self):
        self.Product_dispose(self.__obj)
        return

    def getPointingFactory(self):
        return PointingFactory(self.Product_getPointingFactory(self.__obj))

    def setPointingFactory(self, pointingFactory):
        self.Product_setPointingFactory(self.__obj, pointingFactory.__obj)
        return

    def setGeoCoding(self, geoCoding):
        self.Product_setGeoCoding(self.__obj, geoCoding.__obj)
        return

    def getGeoCoding(self):
        return GeoCoding(self.Product_getGeoCoding(self.__obj))

    def isUsingSingleGeoCoding(self):
        return self.Product_isUsingSingleGeoCoding(self.__obj)

    def transferGeoCodingTo(self, destProduct, subsetDef):
        return self.Product_transferGeoCodingTo(self.__obj, destProduct.__obj, subsetDef.__obj)

    def getSceneRasterWidth(self):
        return self.Product_getSceneRasterWidth(self.__obj)

    def getSceneRasterHeight(self):
        return self.Product_getSceneRasterHeight(self.__obj)

    def getStartTime(self):
        return ProductData_UTC(self.Product_getStartTime(self.__obj))

    def setStartTime(self, startTime):
        self.Product_setStartTime(self.__obj, startTime.__obj)
        return

    def getEndTime(self):
        return ProductData_UTC(self.Product_getEndTime(self.__obj))

    def setEndTime(self, endTime):
        self.Product_setEndTime(self.__obj, endTime.__obj)
        return

    def getMetadataRoot(self):
        return MetadataElement(self.Product_getMetadataRoot(self.__obj))

    def getBandGroup(self):
        return ProductNodeGroup(self.Product_getBandGroup(self.__obj))

    def getTiePointGridGroup(self):
        return ProductNodeGroup(self.Product_getTiePointGridGroup(self.__obj))

    def addTiePointGrid(self, tiePointGrid):
        self.Product_addTiePointGrid(self.__obj, tiePointGrid.__obj)
        return

    def removeTiePointGrid(self, tiePointGrid):
        return self.Product_removeTiePointGrid(self.__obj, tiePointGrid.__obj)

    def getNumTiePointGrids(self):
        return self.Product_getNumTiePointGrids(self.__obj)

    def getTiePointGridAt(self, index):
        return TiePointGrid(self.Product_getTiePointGridAt(self.__obj, index))

    def getTiePointGridNames(self):
        return self.Product_getTiePointGridNames(self.__obj)

    def getTiePointGrids(self):
        return TiePointGrid(self.Product_getTiePointGrids(self.__obj))

    def getTiePointGrid(self, name):
        return TiePointGrid(self.Product_getTiePointGrid(self.__obj, name))

    def containsTiePointGrid(self, name):
        return self.Product_containsTiePointGrid(self.__obj, name)

    def addBand(self, band):
        self.Product_addBand(self.__obj, band.__obj)
        return

    def addBand(self, bandName, dataType):
        return Band(self.Product_addNewBand(self.__obj, bandName, dataType))

    def addBand(self, bandName, expression):
        return Band(self.Product_addComputedBand(self.__obj, bandName, expression))

    def removeBand(self, band):
        return self.Product_removeBand(self.__obj, band.__obj)

    def getNumBands(self):
        return self.Product_getNumBands(self.__obj)

    def getBandAt(self, index):
        return Band(self.Product_getBandAt(self.__obj, index))

    def getBandNames(self):
        return self.Product_getBandNames(self.__obj)

    def getBands(self):
        return Band(self.Product_getBands(self.__obj))

    def getBand(self, name):
        return Band(self.Product_getBand(self.__obj, name))

    def getBandIndex(self, name):
        return self.Product_getBandIndex(self.__obj, name)

    def containsBand(self, name):
        return self.Product_containsBand(self.__obj, name)

    def containsRasterDataNode(self, name):
        return self.Product_containsRasterDataNode(self.__obj, name)

    def getRasterDataNode(self, name):
        return RasterDataNode(self.Product_getRasterDataNode(self.__obj, name))

    def getMaskGroup(self):
        return ProductNodeGroup(self.Product_getMaskGroup(self.__obj))

    def getVectorDataGroup(self):
        return ProductNodeGroup(self.Product_getVectorDataGroup(self.__obj))

    def getFlagCodingGroup(self):
        return ProductNodeGroup(self.Product_getFlagCodingGroup(self.__obj))

    def getIndexCodingGroup(self):
        return ProductNodeGroup(self.Product_getIndexCodingGroup(self.__obj))

    def containsPixel(self, x, y):
        return self.Product_containsPixel(self.__obj, x, y)

    def getGcpGroup(self):
        return PlacemarkGroup(self.Product_getGcpGroup(self.__obj))

    def getPinGroup(self):
        return PlacemarkGroup(self.Product_getPinGroup(self.__obj))

    def isCompatibleProduct(self, product, eps):
        return self.Product_isCompatibleProduct(self.__obj, product.__obj, eps)

    def parseExpression(self, expression):
        return Term(self.Product_parseExpression(self.__obj, expression))

    def acceptVisitor(self, visitor):
        self.Product_acceptVisitor(self.__obj, visitor.__obj)
        return

    def addProductNodeListener(self, listener):
        return self.Product_addProductNodeListener(self.__obj, listener.__obj)

    def removeProductNodeListener(self, listener):
        self.Product_removeProductNodeListener(self.__obj, listener.__obj)
        return

    def getProductNodeListeners(self):
        return ProductNodeListener(self.Product_getProductNodeListeners(self.__obj))

    def getRefNo(self):
        return self.Product_getRefNo(self.__obj)

    def setRefNo(self, refNo):
        self.Product_setRefNo(self.__obj, refNo)
        return

    def resetRefNo(self):
        self.Product_resetRefNo(self.__obj)
        return

    def getProductManager(self):
        return ProductManager(self.Product_getProductManager(self.__obj))

    def createBandArithmeticParser(self):
        return Parser(self.Product_createBandArithmeticParser(self.__obj))

    def createBandArithmeticDefaultNamespace(self):
        return WritableNamespace(self.Product_createBandArithmeticDefaultNamespace(self.__obj))

    def createSubset(self, subsetDef, name, desc):
        return Product(self.Product_createSubset(self.__obj, subsetDef.__obj, name, desc))

    def createProjectedProduct(self, mapInfo, name, desc):
        return Product(self.Product_createProjectedProduct(self.__obj, mapInfo.__obj, name, desc))

    def createFlippedProduct(self, flipType, name, desc):
        return Product(self.Product_createFlippedProduct(self.__obj, flipType, name, desc))

    def setModified(self, modified):
        self.Product_setModified(self.__obj, modified)
        return

    def getQuicklookBandName(self):
        return self.Product_getQuicklookBandName(self.__obj)

    def setQuicklookBandName(self, quicklookBandName):
        self.Product_setQuicklookBandName(self.__obj, quicklookBandName)
        return

    def createPixelInfoString(self, pixelX, pixelY):
        return self.Product_createPixelInfoString(self.__obj, pixelX, pixelY)

    def getRemovedChildNodes(self):
        return ProductNode(self.Product_getRemovedChildNodes(self.__obj))

    def canBeOrthorectified(self):
        return self.Product_canBeOrthorectified(self.__obj)

    def getPreferredTileSize(self):
        return Dimension(self.Product_getPreferredTileSize(self.__obj))

    def setPreferredTileSize(self, tileWidth, tileHeight):
        self.Product_setPreferredTileSize(self.__obj, tileWidth, tileHeight)
        return

    def getAllFlagNames(self):
        return self.Product_getAllFlagNames(self.__obj)

    def getAutoGrouping(self):
        return Product_AutoGrouping(self.Product_getAutoGrouping(self.__obj))

    def setAutoGrouping(self, pattern):
        self.Product_setAutoGrouping(self.__obj, pattern)
        return

    def addMask(self, maskName, expression, description, color, transparency):
        return Mask(self.Product_addComputedMask(self.__obj, maskName, expression, description, color.__obj, transparency))

    def getOwner(self):
        return ProductNode(self.Product_getOwner(self.__obj))

    def getName(self):
        return self.Product_getName(self.__obj)

    def setName(self, name):
        self.Product_setName(self.__obj, name)
        return

    def getDescription(self):
        return self.Product_getDescription(self.__obj)

    def setDescription(self, description):
        self.Product_setDescription(self.__obj, description)
        return

    def isModified(self):
        return self.Product_isModified(self.__obj)

    def toString(self):
        return self.Product_toString(self.__obj)

    def isValidNodeName(name):
        return Product_isValidNodeName(name)

    def getProduct(self):
        return Product(self.Product_getProduct(self.__obj))

    def getDisplayName(self):
        return self.Product_getDisplayName(self.__obj)

    def getProductRefString(self):
        return self.Product_getProductRefString(self.__obj)

    def updateExpression(self, oldExternalName, newExternalName):
        self.Product_updateExpression(self.__obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        self.Product_removeFromFile(self.__obj, productWriter.__obj)
        return


class Point2D:

    def __init__(self, obj):
        self.__obj = obj


class T:

    def __init__(self, obj):
        self.__obj = obj


class ProductVisitor:

    def __init__(self, obj):
        self.__obj = obj


class Scaling:

    def __init__(self, obj):
        self.__obj = obj


class WritableNamespace:

    def __init__(self, obj):
        self.__obj = obj


class MultiLevelImage:

    def __init__(self, obj):
        self.__obj = obj


class ROI:

    def __init__(self, obj):
        self.__obj = obj


class Collection:

    def __init__(self, obj):
        self.__obj = obj


class ProductManager_Listener:

    def __init__(self, obj):
        self.__obj = obj


class GeoTIFFMetadata:

    def __init__(self, obj):
        self.__obj = obj


class ColorPaletteDef:

    def __init__(self, obj):
        self.__obj = obj

    def newColorPaletteDef(minSample, maxSample):
        return ColorPaletteDef(ColorPaletteDef_newColorPaletteDefFromRange(minSample, maxSample))

    def newColorPaletteDef(points, numColors):
        return ColorPaletteDef(ColorPaletteDef_newColorPaletteDefFromPoints(points.__obj, numColors))

    def isDiscrete(self):
        return self.ColorPaletteDef_isDiscrete(self.__obj)

    def setDiscrete(self, discrete):
        self.ColorPaletteDef_setDiscrete(self.__obj, discrete)
        return

    def getNumColors(self):
        return self.ColorPaletteDef_getNumColors(self.__obj)

    def setNumColors(self, numColors):
        self.ColorPaletteDef_setNumColors(self.__obj, numColors)
        return

    def getNumPoints(self):
        return self.ColorPaletteDef_getNumPoints(self.__obj)

    def setNumPoints(self, numPoints):
        self.ColorPaletteDef_setNumPoints(self.__obj, numPoints)
        return

    def isAutoDistribute(self):
        return self.ColorPaletteDef_isAutoDistribute(self.__obj)

    def setAutoDistribute(self, autoDistribute):
        self.ColorPaletteDef_setAutoDistribute(self.__obj, autoDistribute)
        return

    def getPointAt(self, index):
        return ColorPaletteDef_Point(self.ColorPaletteDef_getPointAt(self.__obj, index))

    def getFirstPoint(self):
        return ColorPaletteDef_Point(self.ColorPaletteDef_getFirstPoint(self.__obj))

    def getLastPoint(self):
        return ColorPaletteDef_Point(self.ColorPaletteDef_getLastPoint(self.__obj))

    def getMinDisplaySample(self):
        return self.ColorPaletteDef_getMinDisplaySample(self.__obj)

    def getMaxDisplaySample(self):
        return self.ColorPaletteDef_getMaxDisplaySample(self.__obj)

    def insertPointAfter(self, index, point):
        self.ColorPaletteDef_insertPointAfter(self.__obj, index, point.__obj)
        return

    def createPointAfter(self, index, scaling):
        return self.ColorPaletteDef_createPointAfter(self.__obj, index, scaling.__obj)

    def getCenterColor(c1, c2):
        return Color(ColorPaletteDef_getCenterColor(c1.__obj, c2.__obj))

    def removePointAt(self, index):
        self.ColorPaletteDef_removePointAt(self.__obj, index)
        return

    def addPoint(self, point):
        self.ColorPaletteDef_addPoint(self.__obj, point.__obj)
        return

    def getPoints(self):
        return ColorPaletteDef_Point(self.ColorPaletteDef_getPoints(self.__obj))

    def setPoints(self, points):
        self.ColorPaletteDef_setPoints(self.__obj, points.__obj)
        return

    def getIterator(self):
        return Iterator(self.ColorPaletteDef_getIterator(self.__obj))

    def clone(self):
        return Object(self.ColorPaletteDef_clone(self.__obj))

    def createDeepCopy(self):
        return ColorPaletteDef(self.ColorPaletteDef_createDeepCopy(self.__obj))

    def loadColorPaletteDef(file):
        return ColorPaletteDef(ColorPaletteDef_loadColorPaletteDef(file.__obj))

    def storeColorPaletteDef(colorPaletteDef, file):
        ColorPaletteDef_storeColorPaletteDef(colorPaletteDef.__obj, file.__obj)
        return

    def dispose(self):
        self.ColorPaletteDef_dispose(self.__obj)
        return

    def getColors(self):
        return Color(self.ColorPaletteDef_getColors(self.__obj))

    def createColorPalette(self, scaling):
        return Color(self.ColorPaletteDef_createColorPalette(self.__obj, scaling.__obj))

    def computeColor(self, scaling, sample):
        return Color(self.ColorPaletteDef_computeColor(self.__obj, scaling.__obj, sample))


class MapInfo:

    def __init__(self, obj):
        self.__obj = obj


class ImageInfo:

    def __init__(self, obj):
        self.__obj = obj

    def newImageInfo(colorPaletteDef):
        return ImageInfo(ImageInfo_newImageInfoPalette(colorPaletteDef.__obj))

    def newImageInfo(rgbChannelDef):
        return ImageInfo(ImageInfo_newImageInfoRGB(rgbChannelDef.__obj))

    def getColorPaletteDef(self):
        return ColorPaletteDef(self.ImageInfo_getColorPaletteDef(self.__obj))

    def getRgbChannelDef(self):
        return RGBChannelDef(self.ImageInfo_getRgbChannelDef(self.__obj))

    def getNoDataColor(self):
        return Color(self.ImageInfo_getNoDataColor(self.__obj))

    def setNoDataColor(self, noDataColor):
        self.ImageInfo_setNoDataColor(self.__obj, noDataColor.__obj)
        return

    def setHistogramMatching(self, histogramMatching):
        self.ImageInfo_setHistogramMatching(self.__obj, histogramMatching.__obj)
        return

    def isLogScaled(self):
        return self.ImageInfo_isLogScaled(self.__obj)

    def setLogScaled(self, logScaled):
        self.ImageInfo_setLogScaled(self.__obj, logScaled)
        return

    def getColors(self):
        return Color(self.ImageInfo_getColors(self.__obj))

    def getColorComponentCount(self):
        return self.ImageInfo_getColorComponentCount(self.__obj)

    def createIndexColorModel(self, scaling):
        return IndexColorModel(self.ImageInfo_createIndexColorModel(self.__obj, scaling.__obj))

    def createComponentColorModel(self):
        return ComponentColorModel(self.ImageInfo_createComponentColorModel(self.__obj))

    def clone(self):
        return Object(self.ImageInfo_clone(self.__obj))

    def createDeepCopy(self):
        return ImageInfo(self.ImageInfo_createDeepCopy(self.__obj))

    def dispose(self):
        self.ImageInfo_dispose(self.__obj)
        return

    def setColors(self, colors):
        self.ImageInfo_setColors(self.__obj, colors.__obj)
        return

    def setColorPaletteDef(self, colorPaletteDef, minSample, maxSample, autoDistribute):
        self.ImageInfo_setColorPaletteDef(self.__obj, colorPaletteDef.__obj, minSample, maxSample, autoDistribute)
        return

    def getHistogramMatching(mode):
        return ImageInfo_HistogramMatching(ImageInfo_getHistogramMatching(mode))


class Histogram:

    def __init__(self, obj):
        self.__obj = obj


class String:

    def __init__(self, obj):
        self.__obj = obj


class BufferedImage:

    def __init__(self, obj):
        self.__obj = obj


class SimpleFeature:

    def __init__(self, obj):
        self.__obj = obj


class TiePointGrid:

    def __init__(self, obj):
        self.__obj = obj

    def newTiePointGrid(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints):
        return TiePointGrid(TiePointGrid_newTiePointGrid1(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints))

    def newTiePointGrid(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, discontinuity):
        return TiePointGrid(TiePointGrid_newTiePointGrid2(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, discontinuity))

    def newTiePointGrid(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, containsAngles):
        return TiePointGrid(TiePointGrid_newTiePointGrid3(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, containsAngles))

    def getDiscontinuity(tiePoints):
        return TiePointGrid_getDiscontinuity2(tiePoints)

    def getDiscontinuity(self):
        return self.TiePointGrid_getDiscontinuity1(self.__obj)

    def setDiscontinuity(self, discontinuity):
        self.TiePointGrid_setDiscontinuity(self.__obj, discontinuity)
        return

    def isFloatingPointType(self):
        return self.TiePointGrid_isFloatingPointType(self.__obj)

    def getGeophysicalDataType(self):
        return self.TiePointGrid_getGeophysicalDataType(self.__obj)

    def getSceneRasterData(self):
        return ProductData(self.TiePointGrid_getSceneRasterData(self.__obj))

    def getSceneRasterWidth(self):
        return self.TiePointGrid_getSceneRasterWidth(self.__obj)

    def getSceneRasterHeight(self):
        return self.TiePointGrid_getSceneRasterHeight(self.__obj)

    def getOffsetX(self):
        return self.TiePointGrid_getOffsetX(self.__obj)

    def getOffsetY(self):
        return self.TiePointGrid_getOffsetY(self.__obj)

    def getSubSamplingX(self):
        return self.TiePointGrid_getSubSamplingX(self.__obj)

    def getSubSamplingY(self):
        return self.TiePointGrid_getSubSamplingY(self.__obj)

    def getTiePoints(self):
        return self.TiePointGrid_getTiePoints(self.__obj)

    def getPixelInt(self, x, y):
        return self.TiePointGrid_getPixelInt(self.__obj, x, y)

    def dispose(self):
        self.TiePointGrid_dispose(self.__obj)
        return

    def getPixelFloat(self, x, y):
        return self.TiePointGrid_getPixelFloat2(self.__obj, x, y)

    def getPixelFloat(self, x, y):
        return self.TiePointGrid_getPixelFloat1(self.__obj, x, y)

    def getPixelDouble(self, x, y):
        return self.TiePointGrid_getPixelDouble(self.__obj, x, y)

    def setPixelInt(self, x, y, pixelValue):
        self.TiePointGrid_setPixelInt(self.__obj, x, y, pixelValue)
        return

    def setPixelFloat(self, x, y, pixelValue):
        self.TiePointGrid_setPixelFloat(self.__obj, x, y, pixelValue)
        return

    def setPixelDouble(self, x, y, pixelValue):
        self.TiePointGrid_setPixelDouble(self.__obj, x, y, pixelValue)
        return

    def getPixels(self, x, y, w, h, pixels, pm):
        return self.TiePointGrid_getPixels6(self.__obj, x, y, w, h, pixels, pm.__obj)

    def getPixels(self, x, y, w, h, pixels, pm):
        return self.TiePointGrid_getPixels4(self.__obj, x, y, w, h, pixels, pm.__obj)

    def getPixels(self, x, y, w, h, pixels, pm):
        return self.TiePointGrid_getPixels2(self.__obj, x, y, w, h, pixels, pm.__obj)

    def setPixels(self, x, y, w, h, pixels):
        self.TiePointGrid_setPixels3(self.__obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        self.TiePointGrid_setPixels2(self.__obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        self.TiePointGrid_setPixels1(self.__obj, x, y, w, h, pixels)
        return

    def readPixels(self, x, y, w, h, pixels, pm):
        return self.TiePointGrid_readPixels6(self.__obj, x, y, w, h, pixels, pm.__obj)

    def readPixels(self, x, y, w, h, pixels, pm):
        return self.TiePointGrid_readPixels4(self.__obj, x, y, w, h, pixels, pm.__obj)

    def readPixels(self, x, y, w, h, pixels, pm):
        return self.TiePointGrid_readPixels2(self.__obj, x, y, w, h, pixels, pm.__obj)

    def writePixels(self, x, y, w, h, pixels, pm):
        self.TiePointGrid_writePixels6(self.__obj, x, y, w, h, pixels, pm.__obj)
        return

    def writePixels(self, x, y, w, h, pixels, pm):
        self.TiePointGrid_writePixels4(self.__obj, x, y, w, h, pixels, pm.__obj)
        return

    def writePixels(self, x, y, w, h, pixels, pm):
        self.TiePointGrid_writePixels2(self.__obj, x, y, w, h, pixels, pm.__obj)
        return

    def readRasterData(self, offsetX, offsetY, width, height, rasterData, pm):
        self.TiePointGrid_readRasterData2(self.__obj, offsetX, offsetY, width, height, rasterData.__obj, pm.__obj)
        return

    def readRasterDataFully(self, pm):
        self.TiePointGrid_readRasterDataFully2(self.__obj, pm.__obj)
        return

    def writeRasterData(self, offsetX, offsetY, width, height, rasterData, pm):
        self.TiePointGrid_writeRasterData2(self.__obj, offsetX, offsetY, width, height, rasterData.__obj, pm.__obj)
        return

    def writeRasterDataFully(self, pm):
        self.TiePointGrid_writeRasterDataFully2(self.__obj, pm.__obj)
        return

    def acceptVisitor(self, visitor):
        self.TiePointGrid_acceptVisitor(self.__obj, visitor.__obj)
        return

    def cloneTiePointGrid(self):
        return TiePointGrid(self.TiePointGrid_cloneTiePointGrid(self.__obj))

    def createZenithFromElevationAngleTiePointGrid(elevationAngleGrid):
        return TiePointGrid(TiePointGrid_createZenithFromElevationAngleTiePointGrid(elevationAngleGrid.__obj))

    def createSubset(sourceTiePointGrid, subsetDef):
        return TiePointGrid(TiePointGrid_createSubset(sourceTiePointGrid.__obj, subsetDef.__obj))

    def getRasterWidth(self):
        return self.TiePointGrid_getRasterWidth(self.__obj)

    def getRasterHeight(self):
        return self.TiePointGrid_getRasterHeight(self.__obj)

    def setModified(self, modified):
        self.TiePointGrid_setModified(self.__obj, modified)
        return

    def getGeoCoding(self):
        return GeoCoding(self.TiePointGrid_getGeoCoding(self.__obj))

    def setGeoCoding(self, geoCoding):
        self.TiePointGrid_setGeoCoding(self.__obj, geoCoding.__obj)
        return

    def getPointing(self):
        return Pointing(self.TiePointGrid_getPointing(self.__obj))

    def canBeOrthorectified(self):
        return self.TiePointGrid_canBeOrthorectified(self.__obj)

    def getScalingFactor(self):
        return self.TiePointGrid_getScalingFactor(self.__obj)

    def setScalingFactor(self, scalingFactor):
        self.TiePointGrid_setScalingFactor(self.__obj, scalingFactor)
        return

    def getScalingOffset(self):
        return self.TiePointGrid_getScalingOffset(self.__obj)

    def setScalingOffset(self, scalingOffset):
        self.TiePointGrid_setScalingOffset(self.__obj, scalingOffset)
        return

    def isLog10Scaled(self):
        return self.TiePointGrid_isLog10Scaled(self.__obj)

    def setLog10Scaled(self, log10Scaled):
        self.TiePointGrid_setLog10Scaled(self.__obj, log10Scaled)
        return

    def isScalingApplied(self):
        return self.TiePointGrid_isScalingApplied(self.__obj)

    def isValidMaskProperty(propertyName):
        return TiePointGrid_isValidMaskProperty(propertyName)

    def isNoDataValueSet(self):
        return self.TiePointGrid_isNoDataValueSet(self.__obj)

    def clearNoDataValue(self):
        self.TiePointGrid_clearNoDataValue(self.__obj)
        return

    def isNoDataValueUsed(self):
        return self.TiePointGrid_isNoDataValueUsed(self.__obj)

    def setNoDataValueUsed(self, noDataValueUsed):
        self.TiePointGrid_setNoDataValueUsed(self.__obj, noDataValueUsed)
        return

    def getNoDataValue(self):
        return self.TiePointGrid_getNoDataValue(self.__obj)

    def setNoDataValue(self, noDataValue):
        self.TiePointGrid_setNoDataValue(self.__obj, noDataValue)
        return

    def getGeophysicalNoDataValue(self):
        return self.TiePointGrid_getGeophysicalNoDataValue(self.__obj)

    def setGeophysicalNoDataValue(self, noDataValue):
        self.TiePointGrid_setGeophysicalNoDataValue(self.__obj, noDataValue)
        return

    def getValidPixelExpression(self):
        return self.TiePointGrid_getValidPixelExpression(self.__obj)

    def setValidPixelExpression(self, validPixelExpression):
        self.TiePointGrid_setValidPixelExpression(self.__obj, validPixelExpression)
        return

    def isValidMaskUsed(self):
        return self.TiePointGrid_isValidMaskUsed(self.__obj)

    def resetValidMask(self):
        self.TiePointGrid_resetValidMask(self.__obj)
        return

    def getValidMaskExpression(self):
        return self.TiePointGrid_getValidMaskExpression(self.__obj)

    def updateExpression(self, oldExternalName, newExternalName):
        self.TiePointGrid_updateExpression(self.__obj, oldExternalName, newExternalName)
        return

    def hasRasterData(self):
        return self.TiePointGrid_hasRasterData(self.__obj)

    def getRasterData(self):
        return ProductData(self.TiePointGrid_getRasterData(self.__obj))

    def setRasterData(self, rasterData):
        self.TiePointGrid_setRasterData(self.__obj, rasterData.__obj)
        return

    def loadRasterData(self):
        self.TiePointGrid_loadRasterData1(self.__obj)
        return

    def loadRasterData(self, pm):
        self.TiePointGrid_loadRasterData2(self.__obj, pm.__obj)
        return

    def unloadRasterData(self):
        self.TiePointGrid_unloadRasterData(self.__obj)
        return

    def isPixelValid(self, x, y):
        return self.TiePointGrid_isPixelValid2(self.__obj, x, y)

    def getSampleInt(self, x, y):
        return self.TiePointGrid_getSampleInt(self.__obj, x, y)

    def getSampleFloat(self, x, y):
        return self.TiePointGrid_getSampleFloat(self.__obj, x, y)

    def isPixelValid(self, pixelIndex):
        return self.TiePointGrid_isPixelValid1(self.__obj, pixelIndex)

    def isPixelValid(self, x, y, roi):
        return self.TiePointGrid_isPixelValid3(self.__obj, x, y, roi.__obj)

    def getPixels(self, x, y, w, h, pixels):
        return self.TiePointGrid_getPixels5(self.__obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        return self.TiePointGrid_getPixels3(self.__obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        return self.TiePointGrid_getPixels1(self.__obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return self.TiePointGrid_readPixels5(self.__obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return self.TiePointGrid_readPixels3(self.__obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return self.TiePointGrid_readPixels1(self.__obj, x, y, w, h, pixels)

    def writePixels(self, x, y, w, h, pixels):
        self.TiePointGrid_writePixels5(self.__obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        self.TiePointGrid_writePixels3(self.__obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        self.TiePointGrid_writePixels1(self.__obj, x, y, w, h, pixels)
        return

    def readValidMask(self, x, y, w, h, validMask):
        return self.TiePointGrid_readValidMask(self.__obj, x, y, w, h, validMask)

    def readRasterDataFully(self):
        self.TiePointGrid_readRasterDataFully1(self.__obj)
        return

    def readRasterData(self, offsetX, offsetY, width, height, rasterData):
        self.TiePointGrid_readRasterData1(self.__obj, offsetX, offsetY, width, height, rasterData.__obj)
        return

    def writeRasterDataFully(self):
        self.TiePointGrid_writeRasterDataFully1(self.__obj)
        return

    def writeRasterData(self, offsetX, offsetY, width, height, rasterData):
        self.TiePointGrid_writeRasterData1(self.__obj, offsetX, offsetY, width, height, rasterData.__obj)
        return

    def createCompatibleRasterData(self):
        return ProductData(self.TiePointGrid_createCompatibleRasterData1(self.__obj))

    def createCompatibleSceneRasterData(self):
        return ProductData(self.TiePointGrid_createCompatibleSceneRasterData(self.__obj))

    def createCompatibleRasterData(self, width, height):
        return ProductData(self.TiePointGrid_createCompatibleRasterData2(self.__obj, width, height))

    def isCompatibleRasterData(self, rasterData, w, h):
        return self.TiePointGrid_isCompatibleRasterData(self.__obj, rasterData.__obj, w, h)

    def checkCompatibleRasterData(self, rasterData, w, h):
        self.TiePointGrid_checkCompatibleRasterData(self.__obj, rasterData.__obj, w, h)
        return

    def hasIntPixels(self):
        return self.TiePointGrid_hasIntPixels(self.__obj)

    def createTransectProfileData(self, shape):
        return TransectProfileData(self.TiePointGrid_createTransectProfileData(self.__obj, shape.__obj))

    def getImageInfo(self):
        return ImageInfo(self.TiePointGrid_getImageInfo1(self.__obj))

    def setImageInfo(self, imageInfo):
        self.TiePointGrid_setImageInfo(self.__obj, imageInfo.__obj)
        return

    def fireImageInfoChanged(self):
        self.TiePointGrid_fireImageInfoChanged(self.__obj)
        return

    def getImageInfo(self, pm):
        return ImageInfo(self.TiePointGrid_getImageInfo2(self.__obj, pm.__obj))

    def getImageInfo(self, histoSkipAreas, pm):
        return ImageInfo(self.TiePointGrid_getImageInfo3(self.__obj, histoSkipAreas, pm.__obj))

    def createDefaultImageInfo(self, histoSkipAreas, pm):
        return ImageInfo(self.TiePointGrid_createDefaultImageInfo1(self.__obj, histoSkipAreas, pm.__obj))

    def createDefaultImageInfo(self, histoSkipAreas, histogram):
        return ImageInfo(self.TiePointGrid_createDefaultImageInfo2(self.__obj, histoSkipAreas, histogram.__obj))

    def getOverlayMaskGroup(self):
        return ProductNodeGroup(self.TiePointGrid_getOverlayMaskGroup(self.__obj))

    def createColorIndexedImage(self, pm):
        return BufferedImage(self.TiePointGrid_createColorIndexedImage(self.__obj, pm.__obj))

    def createRgbImage(self, pm):
        return BufferedImage(self.TiePointGrid_createRgbImage(self.__obj, pm.__obj))

    def quantizeRasterData(self, newMin, newMax, gamma, pm):
        return self.TiePointGrid_quantizeRasterData1(self.__obj, newMin, newMax, gamma, pm.__obj)

    def quantizeRasterData(self, newMin, newMax, gamma, samples, offset, stride, pm):
        self.TiePointGrid_quantizeRasterData2(self.__obj, newMin, newMax, gamma, samples, offset, stride, pm.__obj)
        return

    def createPixelValidator(self, lineOffset, roi):
        return IndexValidator(self.TiePointGrid_createPixelValidator(self.__obj, lineOffset, roi.__obj))

    def scale(self, v):
        return self.TiePointGrid_scale(self.__obj, v)

    def scaleInverse(self, v):
        return self.TiePointGrid_scaleInverse(self.__obj, v)

    def getPixelString(self, x, y):
        return self.TiePointGrid_getPixelString(self.__obj, x, y)

    def isSourceImageSet(self):
        return self.TiePointGrid_isSourceImageSet(self.__obj)

    def getSourceImage(self):
        return MultiLevelImage(self.TiePointGrid_getSourceImage(self.__obj))

    def setSourceImage(self, sourceImage):
        self.TiePointGrid_setSourceImage2(self.__obj, sourceImage.__obj)
        return

    def setSourceImage(self, sourceImage):
        self.TiePointGrid_setSourceImage1(self.__obj, sourceImage.__obj)
        return

    def isGeophysicalImageSet(self):
        return self.TiePointGrid_isGeophysicalImageSet(self.__obj)

    def getGeophysicalImage(self):
        return MultiLevelImage(self.TiePointGrid_getGeophysicalImage(self.__obj))

    def isValidMaskImageSet(self):
        return self.TiePointGrid_isValidMaskImageSet(self.__obj)

    def getValidMaskImage(self):
        return MultiLevelImage(self.TiePointGrid_getValidMaskImage(self.__obj))

    def isStxSet(self):
        return self.TiePointGrid_isStxSet(self.__obj)

    def getStx(self):
        return Stx(self.TiePointGrid_getStx1(self.__obj))

    def getStx(self, accurate, pm):
        return Stx(self.TiePointGrid_getStx2(self.__obj, accurate, pm.__obj))

    def setStx(self, stx):
        self.TiePointGrid_setStx(self.__obj, stx.__obj)
        return

    def getValidShape(self):
        return Shape(self.TiePointGrid_getValidShape(self.__obj))

    def getDataType(self):
        return self.TiePointGrid_getDataType(self.__obj)

    def getNumDataElems(self):
        return self.TiePointGrid_getNumDataElems(self.__obj)

    def setData(self, data):
        self.TiePointGrid_setData(self.__obj, data.__obj)
        return

    def getData(self):
        return ProductData(self.TiePointGrid_getData(self.__obj))

    def setDataElems(self, elems):
        self.TiePointGrid_setDataElems(self.__obj, elems.__obj)
        return

    def getDataElems(self):
        return Object(self.TiePointGrid_getDataElems(self.__obj))

    def getDataElemSize(self):
        return self.TiePointGrid_getDataElemSize(self.__obj)

    def setReadOnly(self, readOnly):
        self.TiePointGrid_setReadOnly(self.__obj, readOnly)
        return

    def isReadOnly(self):
        return self.TiePointGrid_isReadOnly(self.__obj)

    def setUnit(self, unit):
        self.TiePointGrid_setUnit(self.__obj, unit)
        return

    def getUnit(self):
        return self.TiePointGrid_getUnit(self.__obj)

    def fireProductNodeDataChanged(self):
        self.TiePointGrid_fireProductNodeDataChanged(self.__obj)
        return

    def getRawStorageSize(self, subsetDef):
        return self.TiePointGrid_getRawStorageSize2(self.__obj, subsetDef.__obj)

    def createCompatibleProductData(self, numElems):
        return ProductData(self.TiePointGrid_createCompatibleProductData(self.__obj, numElems))

    def getOwner(self):
        return ProductNode(self.TiePointGrid_getOwner(self.__obj))

    def getName(self):
        return self.TiePointGrid_getName(self.__obj)

    def setName(self, name):
        self.TiePointGrid_setName(self.__obj, name)
        return

    def getDescription(self):
        return self.TiePointGrid_getDescription(self.__obj)

    def setDescription(self, description):
        self.TiePointGrid_setDescription(self.__obj, description)
        return

    def isModified(self):
        return self.TiePointGrid_isModified(self.__obj)

    def toString(self):
        return self.TiePointGrid_toString(self.__obj)

    def isValidNodeName(name):
        return TiePointGrid_isValidNodeName(name)

    def getProduct(self):
        return Product(self.TiePointGrid_getProduct(self.__obj))

    def getProductReader(self):
        return ProductReader(self.TiePointGrid_getProductReader(self.__obj))

    def getProductWriter(self):
        return ProductWriter(self.TiePointGrid_getProductWriter(self.__obj))

    def getDisplayName(self):
        return self.TiePointGrid_getDisplayName(self.__obj)

    def getProductRefString(self):
        return self.TiePointGrid_getProductRefString(self.__obj)

    def getRawStorageSize(self):
        return self.TiePointGrid_getRawStorageSize1(self.__obj)

    def fireProductNodeChanged(self, propertyName):
        self.TiePointGrid_fireProductNodeChanged1(self.__obj, propertyName)
        return

    def fireProductNodeChanged(self, propertyName, oldValue, newValue):
        self.TiePointGrid_fireProductNodeChanged2(self.__obj, propertyName, oldValue.__obj, newValue.__obj)
        return

    def removeFromFile(self, productWriter):
        self.TiePointGrid_removeFromFile(self.__obj, productWriter.__obj)
        return


class RGBChannelDef:

    def __init__(self, obj):
        self.__obj = obj

    def newRGBChannelDef():
        return RGBChannelDef(RGBChannelDef_newRGBChannelDef())

    def getSourceName(self, index):
        return self.RGBChannelDef_getSourceName(self.__obj, index)

    def setSourceName(self, index, sourceName):
        self.RGBChannelDef_setSourceName(self.__obj, index, sourceName)
        return

    def getSourceNames(self):
        return self.RGBChannelDef_getSourceNames(self.__obj)

    def setSourceNames(self, bandNames):
        self.RGBChannelDef_setSourceNames(self.__obj, bandNames)
        return

    def isAlphaUsed(self):
        return self.RGBChannelDef_isAlphaUsed(self.__obj)

    def isGammaUsed(self, index):
        return self.RGBChannelDef_isGammaUsed(self.__obj, index)

    def getGamma(self, index):
        return self.RGBChannelDef_getGamma(self.__obj, index)

    def setGamma(self, index, gamma):
        self.RGBChannelDef_setGamma(self.__obj, index, gamma)
        return

    def getMinDisplaySample(self, index):
        return self.RGBChannelDef_getMinDisplaySample(self.__obj, index)

    def setMinDisplaySample(self, index, min):
        self.RGBChannelDef_setMinDisplaySample(self.__obj, index, min)
        return

    def getMaxDisplaySample(self, index):
        return self.RGBChannelDef_getMaxDisplaySample(self.__obj, index)

    def setMaxDisplaySample(self, index, max):
        self.RGBChannelDef_setMaxDisplaySample(self.__obj, index, max)
        return

    def clone(self):
        return Object(self.RGBChannelDef_clone(self.__obj))


class ProductSubsetDef:

    def __init__(self, obj):
        self.__obj = obj


class ProductWriter:

    def __init__(self, obj):
        self.__obj = obj

    def getWriterPlugIn(self):
        return ProductWriterPlugIn(self.ProductWriter_getWriterPlugIn(self.__obj))

    def getOutput(self):
        return Object(self.ProductWriter_getOutput(self.__obj))

    def writeProductNodes(self, product, output):
        self.ProductWriter_writeProductNodes(self.__obj, product.__obj, output.__obj)
        return

    def writeBandRasterData(self, sourceBand, sourceOffsetX, sourceOffsetY, sourceWidth, sourceHeight, sourceBuffer, pm):
        self.ProductWriter_writeBandRasterData(self.__obj, sourceBand.__obj, sourceOffsetX, sourceOffsetY, sourceWidth, sourceHeight, sourceBuffer.__obj, pm.__obj)
        return

    def flush(self):
        self.ProductWriter_flush(self.__obj)
        return

    def close(self):
        self.ProductWriter_close(self.__obj)
        return

    def shouldWrite(self, node):
        return self.ProductWriter_shouldWrite(self.__obj, node.__obj)

    def isIncrementalMode(self):
        return self.ProductWriter_isIncrementalMode(self.__obj)

    def setIncrementalMode(self, enabled):
        self.ProductWriter_setIncrementalMode(self.__obj, enabled)
        return

    def deleteOutput(self):
        self.ProductWriter_deleteOutput(self.__obj)
        return

    def removeBand(self, band):
        self.ProductWriter_removeBand(self.__obj, band.__obj)
        return


class MetadataAttribute:

    def __init__(self, obj):
        self.__obj = obj

    def newMetadataAttribute(name, data, readOnly):
        return MetadataAttribute(MetadataAttribute_newMetadataAttribute(name, data.__obj, readOnly))

    def getParentElement(self):
        return MetadataElement(self.MetadataAttribute_getParentElement(self.__obj))

    def equals(self, object):
        return self.MetadataAttribute_equals(self.__obj, object.__obj)

    def acceptVisitor(self, visitor):
        self.MetadataAttribute_acceptVisitor(self.__obj, visitor.__obj)
        return

    def createDeepClone(self):
        return MetadataAttribute(self.MetadataAttribute_createDeepClone(self.__obj))

    def getDataType(self):
        return self.MetadataAttribute_getDataType(self.__obj)

    def isFloatingPointType(self):
        return self.MetadataAttribute_isFloatingPointType(self.__obj)

    def getNumDataElems(self):
        return self.MetadataAttribute_getNumDataElems(self.__obj)

    def setData(self, data):
        self.MetadataAttribute_setData(self.__obj, data.__obj)
        return

    def getData(self):
        return ProductData(self.MetadataAttribute_getData(self.__obj))

    def setDataElems(self, elems):
        self.MetadataAttribute_setDataElems(self.__obj, elems.__obj)
        return

    def getDataElems(self):
        return Object(self.MetadataAttribute_getDataElems(self.__obj))

    def getDataElemSize(self):
        return self.MetadataAttribute_getDataElemSize(self.__obj)

    def setReadOnly(self, readOnly):
        self.MetadataAttribute_setReadOnly(self.__obj, readOnly)
        return

    def isReadOnly(self):
        return self.MetadataAttribute_isReadOnly(self.__obj)

    def setUnit(self, unit):
        self.MetadataAttribute_setUnit(self.__obj, unit)
        return

    def getUnit(self):
        return self.MetadataAttribute_getUnit(self.__obj)

    def fireProductNodeDataChanged(self):
        self.MetadataAttribute_fireProductNodeDataChanged(self.__obj)
        return

    def dispose(self):
        self.MetadataAttribute_dispose(self.__obj)
        return

    def createCompatibleProductData(self, numElems):
        return ProductData(self.MetadataAttribute_createCompatibleProductData(self.__obj, numElems))

    def getOwner(self):
        return ProductNode(self.MetadataAttribute_getOwner(self.__obj))

    def getName(self):
        return self.MetadataAttribute_getName(self.__obj)

    def setName(self, name):
        self.MetadataAttribute_setName(self.__obj, name)
        return

    def getDescription(self):
        return self.MetadataAttribute_getDescription(self.__obj)

    def setDescription(self, description):
        self.MetadataAttribute_setDescription(self.__obj, description)
        return

    def isModified(self):
        return self.MetadataAttribute_isModified(self.__obj)

    def setModified(self, modified):
        self.MetadataAttribute_setModified(self.__obj, modified)
        return

    def toString(self):
        return self.MetadataAttribute_toString(self.__obj)

    def isValidNodeName(name):
        return MetadataAttribute_isValidNodeName(name)

    def getProduct(self):
        return Product(self.MetadataAttribute_getProduct(self.__obj))

    def getProductReader(self):
        return ProductReader(self.MetadataAttribute_getProductReader(self.__obj))

    def getProductWriter(self):
        return ProductWriter(self.MetadataAttribute_getProductWriter(self.__obj))

    def getDisplayName(self):
        return self.MetadataAttribute_getDisplayName(self.__obj)

    def getProductRefString(self):
        return self.MetadataAttribute_getProductRefString(self.__obj)

    def updateExpression(self, oldExternalName, newExternalName):
        self.MetadataAttribute_updateExpression(self.__obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        self.MetadataAttribute_removeFromFile(self.__obj, productWriter.__obj)
        return


class ProgressMonitor:

    def __init__(self, obj):
        self.__obj = obj


class VectorDataNode:

    def __init__(self, obj):
        self.__obj = obj


class GeneralPath:

    def __init__(self, obj):
        self.__obj = obj


class ImageInputStream:

    def __init__(self, obj):
        self.__obj = obj


