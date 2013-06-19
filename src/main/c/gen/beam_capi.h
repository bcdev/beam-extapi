#ifndef BEAM_CAPI_H
#define BEAM_CAPI_H

#ifdef __cplusplus
extern "C" {
#endif

// <<<<<<<< Begin include from CModuleGenerator-stub-types.h
typedef char byte;
typedef unsigned char boolean;
typedef long long dlong;

typedef void* Object;
typedef void* String;
// >>>>>>>> End include from CModuleGenerator-stub-types.h

/* Wrapped API classes */
typedef void* GeoCoding;
typedef void* ProductWriter;
typedef void* GPF;
typedef void* IndexCoding;
typedef void* PixelPos;
typedef void* ProductIO;
typedef void* Placemark;
typedef void* MetadataElement;
typedef void* Product;
typedef void* ColorPaletteDef;
typedef void* ImageInfo;
typedef void* ProductManager;
typedef void* ImageGeometry;
typedef void* Band;
typedef void* PlacemarkGroup;
typedef void* TiePointGrid;
typedef void* AngularDirection;
typedef void* FlagCoding;
typedef void* Map;
typedef void* ProductReader;
typedef void* RGBChannelDef;
typedef void* ProductData;
typedef void* GeoPos;
typedef void* ProductNodeGroup;
typedef void* ProductUtils;
typedef void* MetadataAttribute;


/* Non-API classes used in the API */
typedef void* String;
typedef void* ProgressMonitor;
typedef void* MultiLevelImage;
typedef void* Parser;
typedef void* Term;
typedef void* WritableNamespace;
typedef void* Color;
typedef void* Dimension;
typedef void* Rectangle;
typedef void* RenderingHints;
typedef void* RenderingHints_Key;
typedef void* Shape;
typedef void* AffineTransform;
typedef void* Area;
typedef void* GeneralPath;
typedef void* Point2D;
typedef void* BufferedImage;
typedef void* ComponentColorModel;
typedef void* IndexColorModel;
typedef void* RenderedImage;
typedef void* File;
typedef void* Class;
typedef void* Double;
typedef void* Integer;
typedef void* Object;
typedef void* Collection;
typedef void* Iterator;
typedef void* Set;
typedef void* ImageInputStream;
typedef void* ImageOutputStream;
typedef void* ROI;
typedef void* ProductReaderPlugIn;
typedef void* ProductSubsetDef;
typedef void* ProductWriterPlugIn;
typedef void* BitmaskDef;
typedef void* ColorPaletteDef_Point;
typedef void* ImageInfo_HistogramMatching;
typedef void* Mask;
typedef void* PlacemarkDescriptor;
typedef void* Pointing;
typedef void* PointingFactory;
typedef void* Product_AutoGrouping;
typedef void* ProductData_UTC;
typedef void* ProductManager_Listener;
typedef void* ProductNode;
typedef void* ProductNodeListener;
typedef void* ProductVisitor;
typedef void* RasterDataNode;
typedef void* SampleCoding;
typedef void* Scaling;
typedef void* Stx;
typedef void* TransectProfileData;
typedef void* VectorDataNode;
typedef void* Datum;
typedef void* MapInfo;
typedef void* MapProjection;
typedef void* MapTransform;
typedef void* Operator;
typedef void* OperatorSpiRegistry;
typedef void* BitRaster;
typedef void* GeoTIFFMetadata;
typedef void* Histogram;
typedef void* IndexValidator;
typedef void* SimpleFeature;
typedef void* SimpleFeatureType;
typedef void* CoordinateReferenceSystem;
typedef void* MathTransform;

// <<<<<<<< Begin include from CModuleGenerator-stub-jvm.h
/*
 * Java VM functions that must be used if this module is used in stand-alone
 * mode (= not loaded as shared library by a Java VM).
 */
boolean beam_isJvmCreated();
boolean beam_createJvm(const char* option_strings[], int option_count);
boolean beam_createJvmWithDefaults();
boolean beam_destroyJvm();

// todo - the following functions actually belong in another module because they expect String and Object typedefs to be present

String String_newString(const char* chars);
void Object_delete(Object object);
// >>>>>>>> End include from CModuleGenerator-stub-jvm.h

// <<<<<<<< Begin include from CModuleGenerator-stub-conv.h
/*
 * Functions that must be used after some BEAM C-API library calls in order to
 * release allocated memory.
 */
void beam_deleteCString(char* chars);
void beam_deleteCStringArray(char** array_elems, int array_length);
void beam_deleteCObjectArray(void** array_elems, int array_length);
void beam_deleteCPrimitiveArray(void* array_elems, int array_length);

// >>>>>>>> End include from CModuleGenerator-stub-conv.h


/* Functions for class GeoCoding */

boolean GeoCoding_isCrossingMeridianAt180(GeoCoding _this);
boolean GeoCoding_canGetPixelPos(GeoCoding _this);
boolean GeoCoding_canGetGeoPos(GeoCoding _this);
PixelPos GeoCoding_getPixelPos(GeoCoding _this, GeoPos geoPos, PixelPos pixelPos);
GeoPos GeoCoding_getGeoPos(GeoCoding _this, PixelPos pixelPos, GeoPos geoPos);
Datum GeoCoding_getDatum(GeoCoding _this);
void GeoCoding_dispose(GeoCoding _this);
CoordinateReferenceSystem GeoCoding_getImageCRS(GeoCoding _this);
CoordinateReferenceSystem GeoCoding_getMapCRS(GeoCoding _this);
CoordinateReferenceSystem GeoCoding_getGeoCRS(GeoCoding _this);
MathTransform GeoCoding_getImageToMapTransform(GeoCoding _this);

/* Functions for class ProductWriter */

ProductWriterPlugIn ProductWriter_getWriterPlugIn(ProductWriter _this);
Object ProductWriter_getOutput(ProductWriter _this);
void ProductWriter_writeProductNodes(ProductWriter _this, Product product, Object output);
void ProductWriter_writeBandRasterData(ProductWriter _this, Band sourceBand, int sourceOffsetX, int sourceOffsetY, int sourceWidth, int sourceHeight, ProductData sourceBuffer, ProgressMonitor pm);
void ProductWriter_flush(ProductWriter _this);
void ProductWriter_close(ProductWriter _this);
boolean ProductWriter_shouldWrite(ProductWriter _this, ProductNode node);
boolean ProductWriter_isIncrementalMode(ProductWriter _this);
void ProductWriter_setIncrementalMode(ProductWriter _this, boolean enabled);
void ProductWriter_deleteOutput(ProductWriter _this);
void ProductWriter_removeBand(ProductWriter _this, Band band);

/* Constants of GPF */
extern const char* GPF_DISABLE_TILE_CACHE_PROPERTY;
extern const char* GPF_USE_FILE_TILE_CACHE_PROPERTY;
extern const char* GPF_TILE_COMPUTATION_OBSERVER_PROPERTY;
extern const char* GPF_SOURCE_PRODUCT_FIELD_NAME;
extern const char* GPF_TARGET_PRODUCT_FIELD_NAME;
extern const RenderingHints_Key GPF_KEY_TILE_SIZE;
extern const Map GPF_NO_PARAMS;
extern const Map GPF_NO_SOURCES;

/* Functions for class GPF */

Product GPF_createProductWithoutSourceProducts(const char* operatorName, Map parameters);
Product GPF_createProductFromSourceProduct(const char* operatorName, Map parameters, Product sourceProduct);
Product GPF_createProductFromSourceProducts(const char* operatorName, Map parameters, const Product sourceProductsElems, int sourceProductsLength);
Product GPF_createProductFromNamedSourceProducts(const char* operatorName, Map parameters, Map sourceProducts);
Product GPF_createProductNS(GPF _this, const char* operatorName, Map parameters, Map sourceProducts, RenderingHints renderingHints);
Operator GPF_createOperator(GPF _this, const char* operatorName, Map parameters, Map sourceProducts, RenderingHints renderingHints);
OperatorSpiRegistry GPF_getOperatorSpiRegistry(GPF _this);
void GPF_setOperatorSpiRegistry(GPF _this, OperatorSpiRegistry spiRegistry);
GPF GPF_getDefaultInstance();
void GPF_setDefaultInstance(GPF defaultInstance);
void GPF_writeProduct(Product product, File file, const char* formatName, boolean incremental, ProgressMonitor pm);

/* Constants of IndexCoding */
extern const char* IndexCoding_PROPERTY_NAME_NAME;
extern const char* IndexCoding_PROPERTY_NAME_DESCRIPTION;

/* Functions for class IndexCoding */

IndexCoding IndexCoding_newIndexCoding(const char* name);
MetadataAttribute IndexCoding_getIndex(IndexCoding _this, const char* name);
char** IndexCoding_getIndexNames(IndexCoding _this, int* _resultArrayLength);
MetadataAttribute IndexCoding_addIndex(IndexCoding _this, const char* name, int value, const char* description);
int IndexCoding_getIndexValue(IndexCoding _this, const char* name);
void IndexCoding_acceptVisitor(IndexCoding _this, ProductVisitor visitor);
void IndexCoding_addElement(IndexCoding _this, MetadataElement element);
void IndexCoding_addAttribute(IndexCoding _this, MetadataAttribute attribute);
MetadataAttribute IndexCoding_addSample(IndexCoding _this, const char* name, int value, const char* description);
int IndexCoding_getSampleCount(IndexCoding _this);
char* IndexCoding_getSampleName(IndexCoding _this, int index);
int IndexCoding_getSampleValue(IndexCoding _this, int index);
ProductNodeGroup IndexCoding_getElementGroup(IndexCoding _this);
MetadataElement IndexCoding_getParentElement(IndexCoding _this);
void IndexCoding_addElementAt(IndexCoding _this, MetadataElement element, int index);
boolean IndexCoding_removeElement(IndexCoding _this, MetadataElement element);
int IndexCoding_getNumElements(IndexCoding _this);
MetadataElement IndexCoding_getElementAt(IndexCoding _this, int index);
char** IndexCoding_getElementNames(IndexCoding _this, int* _resultArrayLength);
MetadataElement* IndexCoding_getElements(IndexCoding _this, int* _resultArrayLength);
MetadataElement IndexCoding_getElement(IndexCoding _this, const char* name);
boolean IndexCoding_containsElement(IndexCoding _this, const char* name);
int IndexCoding_getElementIndex(IndexCoding _this, MetadataElement element);
boolean IndexCoding_removeAttribute(IndexCoding _this, MetadataAttribute attribute);
int IndexCoding_getNumAttributes(IndexCoding _this);
MetadataAttribute IndexCoding_getAttributeAt(IndexCoding _this, int index);
char** IndexCoding_getAttributeNames(IndexCoding _this, int* _resultArrayLength);
MetadataAttribute* IndexCoding_getAttributes(IndexCoding _this, int* _resultArrayLength);
MetadataAttribute IndexCoding_getAttribute(IndexCoding _this, const char* name);
boolean IndexCoding_containsAttribute(IndexCoding _this, const char* name);
int IndexCoding_getAttributeIndex(IndexCoding _this, MetadataAttribute attribute);
double IndexCoding_getAttributeDouble(IndexCoding _this, const char* name, double defaultValue);
ProductData_UTC IndexCoding_getAttributeUTC(IndexCoding _this, const char* name, ProductData_UTC defaultValue);
int IndexCoding_getAttributeInt(IndexCoding _this, const char* name, int defaultValue);
void IndexCoding_setAttributeInt(IndexCoding _this, const char* name, int value);
void IndexCoding_setAttributeDouble(IndexCoding _this, const char* name, double value);
void IndexCoding_setAttributeUTC(IndexCoding _this, const char* name, ProductData_UTC value);
char* IndexCoding_getAttributeString(IndexCoding _this, const char* name, const char* defaultValue);
void IndexCoding_setAttributeString(IndexCoding _this, const char* name, const char* value);
void IndexCoding_setModified(IndexCoding _this, boolean modified);
MetadataElement IndexCoding_createDeepClone(IndexCoding _this);
void IndexCoding_dispose(IndexCoding _this);
ProductNode IndexCoding_getOwner(IndexCoding _this);
char* IndexCoding_getName(IndexCoding _this);
void IndexCoding_setName(IndexCoding _this, const char* name);
char* IndexCoding_getDescription(IndexCoding _this);
void IndexCoding_setDescription(IndexCoding _this, const char* description);
boolean IndexCoding_isModified(IndexCoding _this);
char* IndexCoding_toString(IndexCoding _this);
boolean IndexCoding_isValidNodeName(const char* name);
Product IndexCoding_getProduct(IndexCoding _this);
ProductReader IndexCoding_getProductReader(IndexCoding _this);
ProductWriter IndexCoding_getProductWriter(IndexCoding _this);
char* IndexCoding_getDisplayName(IndexCoding _this);
char* IndexCoding_getProductRefString(IndexCoding _this);
void IndexCoding_updateExpression(IndexCoding _this, const char* oldExternalName, const char* newExternalName);
void IndexCoding_removeFromFile(IndexCoding _this, ProductWriter productWriter);
Object IndexCoding_getExtension(IndexCoding _this, Class arg0);

/* Functions for class PixelPos */

PixelPos PixelPos_newPixelPos1();
PixelPos PixelPos_newPixelPos2(float x, float y);
boolean PixelPos_isValid(PixelPos _this);
void PixelPos_setInvalid(PixelPos _this);
double PixelPos_getX(PixelPos _this);
double PixelPos_getY(PixelPos _this);
void PixelPos_setLocation1(PixelPos _this, double arg0, double arg1);
void PixelPos_setLocation2(PixelPos _this, float arg0, float arg1);
char* PixelPos_toString(PixelPos _this);
void PixelPos_setLocation3(PixelPos _this, Point2D arg0);
double PixelPos_distanceSq2(double arg0, double arg1, double arg2, double arg3);
double PixelPos_distance2(double arg0, double arg1, double arg2, double arg3);
double PixelPos_distanceSq1(PixelPos _this, double arg0, double arg1);
double PixelPos_distanceSq3(PixelPos _this, Point2D arg0);
double PixelPos_distance1(PixelPos _this, double arg0, double arg1);
double PixelPos_distance3(PixelPos _this, Point2D arg0);
Object PixelPos_clone(PixelPos _this);
int PixelPos_hashCode(PixelPos _this);
boolean PixelPos_equals(PixelPos _this, Object arg0);

/* Constants of ProductIO */
extern const char* ProductIO_DEFAULT_FORMAT_NAME;

/* Functions for class ProductIO */

ProductReader ProductIO_getProductReader(const char* formatName);
char** ProductIO_getProductWriterExtensions(const char* formatName, int* _resultArrayLength);
ProductWriter ProductIO_getProductWriter(const char* formatName);
Product ProductIO_readProduct(const char* filePath);
ProductReader ProductIO_getProductReaderForFile(File file);
ProductReader ProductIO_getProductReaderForInput(Object input);
void ProductIO_writeProduct(Product product, const char* filePath, const char* formatName);

/* Constants of Placemark */
extern const char* Placemark_PLACEMARK_FEATURE_TYPE_NAME;
extern const char* Placemark_PROPERTY_NAME_LABEL;
extern const char* Placemark_PROPERTY_NAME_TEXT;
extern const char* Placemark_PROPERTY_NAME_PIXELPOS;
extern const char* Placemark_PROPERTY_NAME_GEOPOS;
extern const char* Placemark_PROPERTY_NAME_DATETIME;
extern const char* Placemark_PROPERTY_NAME_STYLE_CSS;
extern const char* Placemark_PROPERTY_NAME_NAME;
extern const char* Placemark_PROPERTY_NAME_DESCRIPTION;

/* Functions for class Placemark */

Placemark Placemark_newPlacemark(PlacemarkDescriptor descriptor, SimpleFeature feature);
Placemark Placemark_createPointPlacemark(PlacemarkDescriptor descriptor, const char* name, const char* label, const char* text, PixelPos pixelPos, GeoPos geoPos, GeoCoding geoCoding);
PlacemarkDescriptor Placemark_getDescriptor(Placemark _this);
SimpleFeature Placemark_getFeature(Placemark _this);
Object Placemark_getAttributeValue(Placemark _this, const char* attributeName);
void Placemark_setAttributeValue(Placemark _this, const char* attributeName, Object attributeValue);
void Placemark_setLabel(Placemark _this, const char* label);
char* Placemark_getLabel(Placemark _this);
void Placemark_setText(Placemark _this, const char* text);
char* Placemark_getText(Placemark _this);
void Placemark_setStyleCss(Placemark _this, const char* styleCss);
char* Placemark_getStyleCss(Placemark _this);
void Placemark_acceptVisitor(Placemark _this, ProductVisitor visitor);
PixelPos Placemark_getPixelPos(Placemark _this);
void Placemark_setPixelPos(Placemark _this, PixelPos pixelPos);
GeoPos Placemark_getGeoPos(Placemark _this);
void Placemark_setGeoPos(Placemark _this, GeoPos geoPos);
void Placemark_updatePositions(Placemark _this);
SimpleFeatureType Placemark_createPinFeatureType();
SimpleFeatureType Placemark_createGcpFeatureType();
SimpleFeatureType Placemark_createGeometryFeatureType();
SimpleFeatureType Placemark_createPointFeatureType(const char* name);
ProductNode Placemark_getOwner(Placemark _this);
char* Placemark_getName(Placemark _this);
void Placemark_setName(Placemark _this, const char* name);
char* Placemark_getDescription(Placemark _this);
void Placemark_setDescription(Placemark _this, const char* description);
boolean Placemark_isModified(Placemark _this);
void Placemark_setModified(Placemark _this, boolean modified);
char* Placemark_toString(Placemark _this);
void Placemark_dispose(Placemark _this);
boolean Placemark_isValidNodeName(const char* name);
Product Placemark_getProduct(Placemark _this);
ProductReader Placemark_getProductReader(Placemark _this);
ProductWriter Placemark_getProductWriter(Placemark _this);
char* Placemark_getDisplayName(Placemark _this);
char* Placemark_getProductRefString(Placemark _this);
void Placemark_updateExpression(Placemark _this, const char* oldExternalName, const char* newExternalName);
void Placemark_removeFromFile(Placemark _this, ProductWriter productWriter);
Object Placemark_getExtension(Placemark _this, Class arg0);

/* Constants of MetadataElement */
extern const char* MetadataElement_PROPERTY_NAME_NAME;
extern const char* MetadataElement_PROPERTY_NAME_DESCRIPTION;

/* Functions for class MetadataElement */

MetadataElement MetadataElement_newMetadataElement(const char* name);
ProductNodeGroup MetadataElement_getElementGroup(MetadataElement _this);
MetadataElement MetadataElement_getParentElement(MetadataElement _this);
void MetadataElement_addElement(MetadataElement _this, MetadataElement element);
void MetadataElement_addElementAt(MetadataElement _this, MetadataElement element, int index);
boolean MetadataElement_removeElement(MetadataElement _this, MetadataElement element);
int MetadataElement_getNumElements(MetadataElement _this);
MetadataElement MetadataElement_getElementAt(MetadataElement _this, int index);
char** MetadataElement_getElementNames(MetadataElement _this, int* _resultArrayLength);
MetadataElement* MetadataElement_getElements(MetadataElement _this, int* _resultArrayLength);
MetadataElement MetadataElement_getElement(MetadataElement _this, const char* name);
boolean MetadataElement_containsElement(MetadataElement _this, const char* name);
int MetadataElement_getElementIndex(MetadataElement _this, MetadataElement element);
void MetadataElement_addAttribute(MetadataElement _this, MetadataAttribute attribute);
boolean MetadataElement_removeAttribute(MetadataElement _this, MetadataAttribute attribute);
int MetadataElement_getNumAttributes(MetadataElement _this);
MetadataAttribute MetadataElement_getAttributeAt(MetadataElement _this, int index);
char** MetadataElement_getAttributeNames(MetadataElement _this, int* _resultArrayLength);
MetadataAttribute* MetadataElement_getAttributes(MetadataElement _this, int* _resultArrayLength);
MetadataAttribute MetadataElement_getAttribute(MetadataElement _this, const char* name);
boolean MetadataElement_containsAttribute(MetadataElement _this, const char* name);
int MetadataElement_getAttributeIndex(MetadataElement _this, MetadataAttribute attribute);
double MetadataElement_getAttributeDouble(MetadataElement _this, const char* name, double defaultValue);
ProductData_UTC MetadataElement_getAttributeUTC(MetadataElement _this, const char* name, ProductData_UTC defaultValue);
int MetadataElement_getAttributeInt(MetadataElement _this, const char* name, int defaultValue);
void MetadataElement_setAttributeInt(MetadataElement _this, const char* name, int value);
void MetadataElement_setAttributeDouble(MetadataElement _this, const char* name, double value);
void MetadataElement_setAttributeUTC(MetadataElement _this, const char* name, ProductData_UTC value);
char* MetadataElement_getAttributeString(MetadataElement _this, const char* name, const char* defaultValue);
void MetadataElement_setAttributeString(MetadataElement _this, const char* name, const char* value);
void MetadataElement_setModified(MetadataElement _this, boolean modified);
void MetadataElement_acceptVisitor(MetadataElement _this, ProductVisitor visitor);
MetadataElement MetadataElement_createDeepClone(MetadataElement _this);
void MetadataElement_dispose(MetadataElement _this);
ProductNode MetadataElement_getOwner(MetadataElement _this);
char* MetadataElement_getName(MetadataElement _this);
void MetadataElement_setName(MetadataElement _this, const char* name);
char* MetadataElement_getDescription(MetadataElement _this);
void MetadataElement_setDescription(MetadataElement _this, const char* description);
boolean MetadataElement_isModified(MetadataElement _this);
char* MetadataElement_toString(MetadataElement _this);
boolean MetadataElement_isValidNodeName(const char* name);
Product MetadataElement_getProduct(MetadataElement _this);
ProductReader MetadataElement_getProductReader(MetadataElement _this);
ProductWriter MetadataElement_getProductWriter(MetadataElement _this);
char* MetadataElement_getDisplayName(MetadataElement _this);
char* MetadataElement_getProductRefString(MetadataElement _this);
void MetadataElement_updateExpression(MetadataElement _this, const char* oldExternalName, const char* newExternalName);
void MetadataElement_removeFromFile(MetadataElement _this, ProductWriter productWriter);
Object MetadataElement_getExtension(MetadataElement _this, Class arg0);

/* Constants of Product */
extern const char* Product_METADATA_ROOT_NAME;
extern const char* Product_HISTORY_ROOT_NAME;
extern const char* Product_PIN_MASK_NAME;
extern const char* Product_GCP_MASK_NAME;
extern const char* Product_PROPERTY_NAME_GEOCODING;
extern const char* Product_PROPERTY_NAME_PRODUCT_TYPE;
extern const char* Product_GEOMETRY_FEATURE_TYPE_NAME;
extern const char* Product_PROPERTY_NAME_NAME;
extern const char* Product_PROPERTY_NAME_DESCRIPTION;

/* Functions for class Product */

Product Product_newProduct(const char* name, const char* type, int sceneRasterWidth, int sceneRasterHeight);
File Product_getFileLocation(Product _this);
void Product_setFileLocation(Product _this, File fileLocation);
char* Product_getProductType(Product _this);
void Product_setProductType(Product _this, const char* productType);
void Product_setProductReader(Product _this, ProductReader reader);
ProductReader Product_getProductReader(Product _this);
void Product_setProductWriter(Product _this, ProductWriter writer);
ProductWriter Product_getProductWriter(Product _this);
void Product_writeHeader(Product _this, Object output);
void Product_closeProductReader(Product _this);
void Product_closeProductWriter(Product _this);
void Product_closeIO(Product _this);
void Product_dispose(Product _this);
PointingFactory Product_getPointingFactory(Product _this);
void Product_setPointingFactory(Product _this, PointingFactory pointingFactory);
void Product_setGeoCoding(Product _this, GeoCoding geoCoding);
GeoCoding Product_getGeoCoding(Product _this);
boolean Product_isUsingSingleGeoCoding(Product _this);
boolean Product_transferGeoCodingTo(Product _this, Product destProduct, ProductSubsetDef subsetDef);
int Product_getSceneRasterWidth(Product _this);
int Product_getSceneRasterHeight(Product _this);
ProductData_UTC Product_getStartTime(Product _this);
void Product_setStartTime(Product _this, ProductData_UTC startTime);
ProductData_UTC Product_getEndTime(Product _this);
void Product_setEndTime(Product _this, ProductData_UTC endTime);
MetadataElement Product_getMetadataRoot(Product _this);
ProductNodeGroup Product_getBandGroup(Product _this);
ProductNodeGroup Product_getTiePointGridGroup(Product _this);
void Product_addTiePointGrid(Product _this, TiePointGrid tiePointGrid);
boolean Product_removeTiePointGrid(Product _this, TiePointGrid tiePointGrid);
int Product_getNumTiePointGrids(Product _this);
TiePointGrid Product_getTiePointGridAt(Product _this, int index);
char** Product_getTiePointGridNames(Product _this, int* _resultArrayLength);
TiePointGrid* Product_getTiePointGrids(Product _this, int* _resultArrayLength);
TiePointGrid Product_getTiePointGrid(Product _this, const char* name);
boolean Product_containsTiePointGrid(Product _this, const char* name);
void Product_addBand(Product _this, Band band);
Band Product_addNewBand(Product _this, const char* bandName, int dataType);
Band Product_addComputedBand(Product _this, const char* bandName, const char* expression);
boolean Product_removeBand(Product _this, Band band);
int Product_getNumBands(Product _this);
Band Product_getBandAt(Product _this, int index);
char** Product_getBandNames(Product _this, int* _resultArrayLength);
Band* Product_getBands(Product _this, int* _resultArrayLength);
Band Product_getBand(Product _this, const char* name);
int Product_getBandIndex(Product _this, const char* name);
boolean Product_containsBand(Product _this, const char* name);
boolean Product_containsRasterDataNode(Product _this, const char* name);
RasterDataNode Product_getRasterDataNode(Product _this, const char* name);
ProductNodeGroup Product_getMaskGroup(Product _this);
ProductNodeGroup Product_getVectorDataGroup(Product _this);
ProductNodeGroup Product_getFlagCodingGroup(Product _this);
ProductNodeGroup Product_getIndexCodingGroup(Product _this);
boolean Product_containsPixel(Product _this, float x, float y);
PlacemarkGroup Product_getGcpGroup(Product _this);
PlacemarkGroup Product_getPinGroup(Product _this);
boolean Product_isCompatibleProduct(Product _this, Product product, float eps);
Term Product_parseExpression(Product _this, const char* expression);
void Product_acceptVisitor(Product _this, ProductVisitor visitor);
boolean Product_addProductNodeListener(Product _this, ProductNodeListener listener);
void Product_removeProductNodeListener(Product _this, ProductNodeListener listener);
ProductNodeListener* Product_getProductNodeListeners(Product _this, int* _resultArrayLength);
int Product_getRefNo(Product _this);
void Product_setRefNo(Product _this, int refNo);
void Product_resetRefNo(Product _this);
ProductManager Product_getProductManager(Product _this);
Parser Product_createBandArithmeticParser(Product _this);
WritableNamespace Product_createBandArithmeticDefaultNamespace(Product _this);
Product Product_createSubset(Product _this, ProductSubsetDef subsetDef, const char* name, const char* desc);
Product Product_createFlippedProduct(Product _this, int flipType, const char* name, const char* desc);
void Product_setModified(Product _this, boolean modified);
char* Product_getQuicklookBandName(Product _this);
void Product_setQuicklookBandName(Product _this, const char* quicklookBandName);
char* Product_createPixelInfoString(Product _this, int pixelX, int pixelY);
ProductNode* Product_getRemovedChildNodes(Product _this, int* _resultArrayLength);
boolean Product_canBeOrthorectified(Product _this);
Dimension Product_getPreferredTileSize(Product _this);
void Product_setPreferredTileSize(Product _this, int tileWidth, int tileHeight);
char** Product_getAllFlagNames(Product _this, int* _resultArrayLength);
Product_AutoGrouping Product_getAutoGrouping(Product _this);
void Product_setAutoGrouping(Product _this, const char* pattern);
Mask Product_addComputedMask(Product _this, const char* maskName, const char* expression, const char* description, Color color, double transparency);
void Product_addBitmaskDef(Product _this, BitmaskDef bitmaskDef);
char** Product_getBitmaskDefNames(Product _this, int* _resultArrayLength);
BitmaskDef Product_getBitmaskDef(Product _this, const char* name);
BitRaster Product_getValidMask(Product _this, const char* id);
void Product_setValidMask(Product _this, const char* id, BitRaster validMask);
BitRaster Product_createValidMask2(Product _this, const char* expression, ProgressMonitor pm);
BitRaster Product_createValidMask1(Product _this, Term term, ProgressMonitor pm);
void Product_readBitmask2(Product _this, int offsetX, int offsetY, int width, int height, Term bitmaskTerm, const boolean* bitmaskElems, int bitmaskLength, ProgressMonitor pm);
void Product_readBitmask1(Product _this, int offsetX, int offsetY, int width, int height, Term bitmaskTerm, const byte* bitmaskElems, int bitmaskLength, byte trueValue, byte falseValue, ProgressMonitor pm);
ProductNode Product_getOwner(Product _this);
char* Product_getName(Product _this);
void Product_setName(Product _this, const char* name);
char* Product_getDescription(Product _this);
void Product_setDescription(Product _this, const char* description);
boolean Product_isModified(Product _this);
char* Product_toString(Product _this);
boolean Product_isValidNodeName(const char* name);
Product Product_getProduct(Product _this);
char* Product_getDisplayName(Product _this);
char* Product_getProductRefString(Product _this);
void Product_updateExpression(Product _this, const char* oldExternalName, const char* newExternalName);
void Product_removeFromFile(Product _this, ProductWriter productWriter);
Object Product_getExtension(Product _this, Class arg0);

/* Functions for class ColorPaletteDef */

ColorPaletteDef ColorPaletteDef_newColorPaletteDefFromRange(double minSample, double maxSample);
ColorPaletteDef ColorPaletteDef_newColorPaletteDefFromPoints(const ColorPaletteDef_Point pointsElems, int pointsLength, int numColors);
boolean ColorPaletteDef_isDiscrete(ColorPaletteDef _this);
void ColorPaletteDef_setDiscrete(ColorPaletteDef _this, boolean discrete);
int ColorPaletteDef_getNumColors(ColorPaletteDef _this);
void ColorPaletteDef_setNumColors(ColorPaletteDef _this, int numColors);
int ColorPaletteDef_getNumPoints(ColorPaletteDef _this);
void ColorPaletteDef_setNumPoints(ColorPaletteDef _this, int numPoints);
boolean ColorPaletteDef_isAutoDistribute(ColorPaletteDef _this);
void ColorPaletteDef_setAutoDistribute(ColorPaletteDef _this, boolean autoDistribute);
ColorPaletteDef_Point ColorPaletteDef_getPointAt(ColorPaletteDef _this, int index);
ColorPaletteDef_Point ColorPaletteDef_getFirstPoint(ColorPaletteDef _this);
ColorPaletteDef_Point ColorPaletteDef_getLastPoint(ColorPaletteDef _this);
double ColorPaletteDef_getMinDisplaySample(ColorPaletteDef _this);
double ColorPaletteDef_getMaxDisplaySample(ColorPaletteDef _this);
void ColorPaletteDef_insertPointAfter(ColorPaletteDef _this, int index, ColorPaletteDef_Point point);
boolean ColorPaletteDef_createPointAfter(ColorPaletteDef _this, int index, Scaling scaling);
Color ColorPaletteDef_getCenterColor(Color c1, Color c2);
void ColorPaletteDef_removePointAt(ColorPaletteDef _this, int index);
void ColorPaletteDef_addPoint(ColorPaletteDef _this, ColorPaletteDef_Point point);
ColorPaletteDef_Point* ColorPaletteDef_getPoints(ColorPaletteDef _this, int* _resultArrayLength);
void ColorPaletteDef_setPoints(ColorPaletteDef _this, const ColorPaletteDef_Point pointsElems, int pointsLength);
Iterator ColorPaletteDef_getIterator(ColorPaletteDef _this);
Object ColorPaletteDef_clone(ColorPaletteDef _this);
ColorPaletteDef ColorPaletteDef_createDeepCopy(ColorPaletteDef _this);
ColorPaletteDef ColorPaletteDef_loadColorPaletteDef(File file);
void ColorPaletteDef_storeColorPaletteDef(ColorPaletteDef colorPaletteDef, File file);
void ColorPaletteDef_dispose(ColorPaletteDef _this);
Color* ColorPaletteDef_getColors(ColorPaletteDef _this, int* _resultArrayLength);
Color* ColorPaletteDef_createColorPalette(ColorPaletteDef _this, Scaling scaling, int* _resultArrayLength);
Color ColorPaletteDef_computeColor(ColorPaletteDef _this, Scaling scaling, double sample);

/* Constants of ImageInfo */
extern const Color ImageInfo_NO_COLOR;
extern const char* ImageInfo_HISTOGRAM_MATCHING_OFF;
extern const char* ImageInfo_HISTOGRAM_MATCHING_EQUALIZE;
extern const char* ImageInfo_HISTOGRAM_MATCHING_NORMALIZE;

/* Functions for class ImageInfo */

ImageInfo ImageInfo_newImageInfoPalette(ColorPaletteDef colorPaletteDef);
ImageInfo ImageInfo_newImageInfoRGB(RGBChannelDef rgbChannelDef);
ColorPaletteDef ImageInfo_getColorPaletteDef(ImageInfo _this);
RGBChannelDef ImageInfo_getRgbChannelDef(ImageInfo _this);
Color ImageInfo_getNoDataColor(ImageInfo _this);
void ImageInfo_setNoDataColor(ImageInfo _this, Color noDataColor);
void ImageInfo_setHistogramMatching(ImageInfo _this, ImageInfo_HistogramMatching histogramMatching);
boolean ImageInfo_isLogScaled(ImageInfo _this);
void ImageInfo_setLogScaled(ImageInfo _this, boolean logScaled);
Color* ImageInfo_getColors(ImageInfo _this, int* _resultArrayLength);
int ImageInfo_getColorComponentCount(ImageInfo _this);
IndexColorModel ImageInfo_createIndexColorModel(ImageInfo _this, Scaling scaling);
ComponentColorModel ImageInfo_createComponentColorModel(ImageInfo _this);
Object ImageInfo_clone(ImageInfo _this);
ImageInfo ImageInfo_createDeepCopy(ImageInfo _this);
void ImageInfo_dispose(ImageInfo _this);
void ImageInfo_setColors(ImageInfo _this, const Color colorsElems, int colorsLength);
void ImageInfo_setColorPaletteDef(ImageInfo _this, ColorPaletteDef colorPaletteDef, double minSample, double maxSample, boolean autoDistribute);
ImageInfo_HistogramMatching ImageInfo_getHistogramMatching(const char* mode);

/* Functions for class ProductManager */

ProductManager ProductManager_newProductManager();
int ProductManager_getProductCount(ProductManager _this);
Product ProductManager_getProduct(ProductManager _this, int index);
char** ProductManager_getProductDisplayNames(ProductManager _this, int* _resultArrayLength);
char** ProductManager_getProductNames(ProductManager _this, int* _resultArrayLength);
Product* ProductManager_getProducts(ProductManager _this, int* _resultArrayLength);
Product ProductManager_getProductByDisplayName(ProductManager _this, const char* displayName);
Product ProductManager_getProductByRefNo(ProductManager _this, int refNo);
Product ProductManager_getProductByName(ProductManager _this, const char* name);
int ProductManager_getProductIndex(ProductManager _this, Product product);
boolean ProductManager_containsProduct(ProductManager _this, const char* name);
boolean ProductManager_contains(ProductManager _this, Product product);
void ProductManager_addProduct(ProductManager _this, Product product);
boolean ProductManager_removeProduct(ProductManager _this, Product product);
void ProductManager_removeAllProducts(ProductManager _this);
boolean ProductManager_addListener(ProductManager _this, ProductManager_Listener listener);
boolean ProductManager_removeListener(ProductManager _this, ProductManager_Listener listener);

/* Functions for class ImageGeometry */

ImageGeometry ImageGeometry_newImageGeometry(Rectangle bounds, CoordinateReferenceSystem mapCrs, AffineTransform image2map);
AffineTransform ImageGeometry_getImage2MapTransform(ImageGeometry _this);
Rectangle ImageGeometry_getImageRect(ImageGeometry _this);
CoordinateReferenceSystem ImageGeometry_getMapCrs(ImageGeometry _this);
void ImageGeometry_changeYAxisDirection(ImageGeometry _this);
Point2D ImageGeometry_calculateEastingNorthing(Product sourceProduct, CoordinateReferenceSystem targetCrs, double referencePixelX, double referencePixelY, double pixelSizeX, double pixelSizeY);
Rectangle ImageGeometry_calculateProductSize(Product sourceProduct, CoordinateReferenceSystem targetCrs, double pixelSizeX, double pixelSizeY);
ImageGeometry ImageGeometry_createTargetGeometry(Product sourceProduct, CoordinateReferenceSystem targetCrs, Double pixelSizeX, Double pixelSizeY, Integer width, Integer height, Double orientation, Double easting, Double northing, Double referencePixelX, Double referencePixelY);
ImageGeometry ImageGeometry_createCollocationTargetGeometry(Product targetProduct, Product collocationProduct);

/* Constants of Band */
extern const char* Band_PROPERTY_NAME_SAMPLE_CODING;
extern const char* Band_PROPERTY_NAME_SOLAR_FLUX;
extern const char* Band_PROPERTY_NAME_SPECTRAL_BAND_INDEX;
extern const char* Band_PROPERTY_NAME_SPECTRAL_BANDWIDTH;
extern const char* Band_PROPERTY_NAME_SPECTRAL_WAVELENGTH;
extern const char* Band_VIEW_MODE_ORTHO;
extern const char* Band_VIEW_MODE_FORWARD;
extern const char* Band_VIEW_MODE_NADIR;
extern const char* Band_PROPERTY_NAME_IMAGE_INFO;
extern const char* Band_PROPERTY_NAME_LOG_10_SCALED;
extern const char* Band_PROPERTY_NAME_ROI_DEFINITION;
extern const char* Band_PROPERTY_NAME_SCALING_FACTOR;
extern const char* Band_PROPERTY_NAME_SCALING_OFFSET;
extern const char* Band_PROPERTY_NAME_NO_DATA_VALUE;
extern const char* Band_PROPERTY_NAME_NO_DATA_VALUE_USED;
extern const char* Band_PROPERTY_NAME_VALID_PIXEL_EXPRESSION;
extern const char* Band_PROPERTY_NAME_GEOCODING;
extern const char* Band_PROPERTY_NAME_STX;
extern const char* Band_NO_DATA_TEXT;
extern const char* Band_INVALID_POS_TEXT;
extern const char* Band_IO_ERROR_TEXT;
extern const char* Band_PROPERTY_NAME_DATA;
extern const char* Band_PROPERTY_NAME_READ_ONLY;
extern const char* Band_PROPERTY_NAME_SYNTHETIC;
extern const char* Band_PROPERTY_NAME_UNIT;
extern const char* Band_PROPERTY_NAME_NAME;
extern const char* Band_PROPERTY_NAME_DESCRIPTION;

/* Functions for class Band */

Band Band_newBand(const char* name, int dataType, int width, int height);
FlagCoding Band_getFlagCoding(Band _this);
boolean Band_isFlagBand(Band _this);
IndexCoding Band_getIndexCoding(Band _this);
boolean Band_isIndexBand(Band _this);
SampleCoding Band_getSampleCoding(Band _this);
void Band_setSampleCoding(Band _this, SampleCoding sampleCoding);
int Band_getSpectralBandIndex(Band _this);
void Band_setSpectralBandIndex(Band _this, int spectralBandIndex);
float Band_getSpectralWavelength(Band _this);
void Band_setSpectralWavelength(Band _this, float spectralWavelength);
float Band_getSpectralBandwidth(Band _this);
void Band_setSpectralBandwidth(Band _this, float spectralBandwidth);
float Band_getSolarFlux(Band _this);
void Band_setSolarFlux(Band _this, float solarFlux);
void Band_acceptVisitor(Band _this, ProductVisitor visitor);
char* Band_toString(Band _this);
void Band_removeFromFile(Band _this, ProductWriter productWriter);
void Band_dispose(Band _this);
char* Band_getViewModeId(Band _this, const char* bandName);
int Band_computeBand(Band _this, const char* expression, const char* validMaskExpression, const Product sourceProductsElems, int sourceProductsLength, int defaultProductIndex, boolean checkInvalids, boolean useInvalidValue, double noDataValue, ProgressMonitor pm);
ProductData Band_getSceneRasterData(Band _this);
int Band_getPixelInt(Band _this, int x, int y);
float Band_getPixelFloat(Band _this, int x, int y);
double Band_getPixelDouble(Band _this, int x, int y);
void Band_setPixelInt(Band _this, int x, int y, int pixelValue);
void Band_setPixelFloat(Band _this, int x, int y, float pixelValue);
void Band_setPixelDouble(Band _this, int x, int y, double pixelValue);
void Band_setPixelsInt(Band _this, int x, int y, int w, int h, const int* pixelsElems, int pixelsLength);
void Band_setPixelsFloat(Band _this, int x, int y, int w, int h, const float* pixelsElems, int pixelsLength);
void Band_setPixelsDouble(Band _this, int x, int y, int w, int h, const double* pixelsElems, int pixelsLength);
void Band_ensureRasterData(Band _this);
void Band_unloadRasterData(Band _this);
int Band_getSceneRasterWidth(Band _this);
int Band_getSceneRasterHeight(Band _this);
int Band_getRasterWidth(Band _this);
int Band_getRasterHeight(Band _this);
void Band_setModified(Band _this, boolean modified);
GeoCoding Band_getGeoCoding(Band _this);
void Band_setGeoCoding(Band _this, GeoCoding geoCoding);
Pointing Band_getPointing(Band _this);
boolean Band_canBeOrthorectified(Band _this);
boolean Band_isFloatingPointType(Band _this);
int Band_getGeophysicalDataType(Band _this);
double Band_getScalingFactor(Band _this);
void Band_setScalingFactor(Band _this, double scalingFactor);
double Band_getScalingOffset(Band _this);
void Band_setScalingOffset(Band _this, double scalingOffset);
boolean Band_isLog10Scaled(Band _this);
void Band_setLog10Scaled(Band _this, boolean log10Scaled);
boolean Band_isScalingApplied(Band _this);
boolean Band_isValidMaskProperty(const char* propertyName);
boolean Band_isNoDataValueSet(Band _this);
void Band_clearNoDataValue(Band _this);
boolean Band_isNoDataValueUsed(Band _this);
void Band_setNoDataValueUsed(Band _this, boolean noDataValueUsed);
double Band_getNoDataValue(Band _this);
void Band_setNoDataValue(Band _this, double noDataValue);
double Band_getGeophysicalNoDataValue(Band _this);
void Band_setGeophysicalNoDataValue(Band _this, double noDataValue);
char* Band_getValidPixelExpression(Band _this);
void Band_setValidPixelExpression(Band _this, const char* validPixelExpression);
boolean Band_isValidMaskUsed(Band _this);
void Band_resetValidMask(Band _this);
char* Band_getValidMaskExpression(Band _this);
void Band_updateExpression(Band _this, const char* oldExternalName, const char* newExternalName);
boolean Band_hasRasterData(Band _this);
ProductData Band_getRasterData(Band _this);
void Band_setRasterData(Band _this, ProductData rasterData);
void Band_loadRasterData(Band _this);
boolean Band_isPixelValid(Band _this, int x, int y);
int Band_getSampleInt(Band _this, int x, int y);
float Band_getSampleFloat(Band _this, int x, int y);
int* Band_getPixelsInt(Band _this, int x, int y, int w, int h, const int* pixelsElems, int pixelsLength, int* _resultArrayLength);
float* Band_getPixelsFloat(Band _this, int x, int y, int w, int h, const float* pixelsElems, int pixelsLength, int* _resultArrayLength);
double* Band_getPixelsDouble(Band _this, int x, int y, int w, int h, const double* pixelsElems, int pixelsLength, int* _resultArrayLength);
int* Band_readPixelsInt(Band _this, int x, int y, int w, int h, int* pixelsElems, int pixelsLength, int* _resultArrayLength);
float* Band_readPixelsFloat(Band _this, int x, int y, int w, int h, float* pixelsElems, int pixelsLength, int* _resultArrayLength);
double* Band_readPixelsDouble(Band _this, int x, int y, int w, int h, double* pixelsElems, int pixelsLength, int* _resultArrayLength);
void Band_writePixelsInt(Band _this, int x, int y, int w, int h, const int* pixelsElems, int pixelsLength);
void Band_writePixelsFloat(Band _this, int x, int y, int w, int h, const float* pixelsElems, int pixelsLength);
void Band_writePixelsDouble(Band _this, int x, int y, int w, int h, const double* pixelsElems, int pixelsLength);
boolean* Band_readValidMask(Band _this, int x, int y, int w, int h, boolean* validMaskElems, int validMaskLength, int* _resultArrayLength);
void Band_writeRasterDataFully(Band _this);
void Band_writeRasterData(Band _this, int offsetX, int offsetY, int width, int height, ProductData rasterData);
ProductData Band_createCompatibleRasterData(Band _this);
ProductData Band_createCompatibleSceneRasterData(Band _this);
ProductData Band_createCompatibleRasterDataForRect(Band _this, int width, int height);
boolean Band_isCompatibleRasterData(Band _this, ProductData rasterData, int w, int h);
void Band_checkCompatibleRasterData(Band _this, ProductData rasterData, int w, int h);
boolean Band_hasIntPixels(Band _this);
TransectProfileData Band_createTransectProfileData(Band _this, Shape shape);
ImageInfo Band_getImageInfo(Band _this);
void Band_setImageInfo(Band _this, ImageInfo imageInfo);
void Band_fireImageInfoChanged(Band _this);
ImageInfo Band_createDefaultImageInfo(Band _this, const double* histoSkipAreasElems, int histoSkipAreasLength, Histogram histogram);
ProductNodeGroup Band_getOverlayMaskGroup(Band _this);
BufferedImage Band_createColorIndexedImage(Band _this, ProgressMonitor pm);
BufferedImage Band_createRgbImage(Band _this, ProgressMonitor pm);
IndexValidator Band_createPixelValidator(Band _this, int lineOffset, ROI roi);
double Band_scale(Band _this, double v);
double Band_scaleInverse(Band _this, double v);
char* Band_getPixelString(Band _this, int x, int y);
boolean Band_isSourceImageSet(Band _this);
MultiLevelImage Band_getSourceImage(Band _this);
boolean Band_isGeophysicalImageSet(Band _this);
MultiLevelImage Band_getGeophysicalImage(Band _this);
boolean Band_isValidMaskImageSet(Band _this);
MultiLevelImage Band_getValidMaskImage(Band _this);
boolean Band_isStxSet(Band _this);
Stx Band_getStx(Band _this);
void Band_setStx(Band _this, Stx stx);
Shape Band_getValidShape(Band _this);
ProductNodeGroup Band_getRoiMaskGroup(Band _this);
int Band_getDataType(Band _this);
dlong Band_getNumDataElems(Band _this);
void Band_setData(Band _this, ProductData data);
ProductData Band_getData(Band _this);
void Band_setDataElems(Band _this, Object elems);
Object Band_getDataElems(Band _this);
int Band_getDataElemSize(Band _this);
void Band_setReadOnly(Band _this, boolean readOnly);
boolean Band_isReadOnly(Band _this);
void Band_setUnit(Band _this, const char* unit);
char* Band_getUnit(Band _this);
boolean Band_isSynthetic(Band _this);
void Band_setSynthetic(Band _this, boolean synthetic);
void Band_fireProductNodeDataChanged(Band _this);
ProductData Band_createCompatibleProductData(Band _this, int numElems);
ProductNode Band_getOwner(Band _this);
char* Band_getName(Band _this);
void Band_setName(Band _this, const char* name);
char* Band_getDescription(Band _this);
void Band_setDescription(Band _this, const char* description);
boolean Band_isModified(Band _this);
boolean Band_isValidNodeName(const char* name);
Product Band_getProduct(Band _this);
ProductReader Band_getProductReader(Band _this);
ProductWriter Band_getProductWriter(Band _this);
char* Band_getDisplayName(Band _this);
char* Band_getProductRefString(Band _this);
Object Band_getExtension(Band _this, Class arg0);

/* Constants of PlacemarkGroup */
extern const char* PlacemarkGroup_PROPERTY_NAME_NAME;
extern const char* PlacemarkGroup_PROPERTY_NAME_DESCRIPTION;

/* Functions for class PlacemarkGroup */

VectorDataNode PlacemarkGroup_getVectorDataNode(PlacemarkGroup _this);
Placemark PlacemarkGroup_getPlacemark(PlacemarkGroup _this, SimpleFeature feature);
boolean PlacemarkGroup_add3(PlacemarkGroup _this, Placemark placemark);
void PlacemarkGroup_add1(PlacemarkGroup _this, int index, Placemark placemark);
boolean PlacemarkGroup_remove1(PlacemarkGroup _this, Placemark placemark);
void PlacemarkGroup_dispose(PlacemarkGroup _this);
boolean PlacemarkGroup_isTakingOverNodeOwnership(PlacemarkGroup _this);
int PlacemarkGroup_getNodeCount(PlacemarkGroup _this);
ProductNode PlacemarkGroup_get1(PlacemarkGroup _this, int index);
char** PlacemarkGroup_getNodeDisplayNames(PlacemarkGroup _this, int* _resultArrayLength);
char** PlacemarkGroup_getNodeNames(PlacemarkGroup _this, int* _resultArrayLength);
ProductNode* PlacemarkGroup_toArray1(PlacemarkGroup _this, int* _resultArrayLength);
ProductNode PlacemarkGroup_toArray2(PlacemarkGroup _this, const ProductNode arrayElems, int arrayLength);
int PlacemarkGroup_indexOf1(PlacemarkGroup _this, const char* name);
int PlacemarkGroup_indexOf2(PlacemarkGroup _this, ProductNode element);
ProductNode PlacemarkGroup_getByDisplayName(PlacemarkGroup _this, const char* displayName);
ProductNode PlacemarkGroup_get2(PlacemarkGroup _this, const char* name);
boolean PlacemarkGroup_contains1(PlacemarkGroup _this, const char* name);
boolean PlacemarkGroup_contains2(PlacemarkGroup _this, ProductNode node);
boolean PlacemarkGroup_add4(PlacemarkGroup _this, ProductNode node);
void PlacemarkGroup_add2(PlacemarkGroup _this, int index, ProductNode node);
boolean PlacemarkGroup_remove2(PlacemarkGroup _this, ProductNode node);
void PlacemarkGroup_removeAll(PlacemarkGroup _this);
void PlacemarkGroup_clearRemovedList(PlacemarkGroup _this);
Collection PlacemarkGroup_getRemovedNodes(PlacemarkGroup _this);
dlong PlacemarkGroup_getRawStorageSize2(PlacemarkGroup _this, ProductSubsetDef subsetDef);
void PlacemarkGroup_setModified(PlacemarkGroup _this, boolean modified);
void PlacemarkGroup_acceptVisitor(PlacemarkGroup _this, ProductVisitor visitor);
void PlacemarkGroup_updateExpression(PlacemarkGroup _this, const char* oldExternalName, const char* newExternalName);
ProductNode PlacemarkGroup_getOwner(PlacemarkGroup _this);
char* PlacemarkGroup_getName(PlacemarkGroup _this);
void PlacemarkGroup_setName(PlacemarkGroup _this, const char* name);
char* PlacemarkGroup_getDescription(PlacemarkGroup _this);
void PlacemarkGroup_setDescription(PlacemarkGroup _this, const char* description);
boolean PlacemarkGroup_isModified(PlacemarkGroup _this);
char* PlacemarkGroup_toString(PlacemarkGroup _this);
boolean PlacemarkGroup_isValidNodeName(const char* name);
Product PlacemarkGroup_getProduct(PlacemarkGroup _this);
ProductReader PlacemarkGroup_getProductReader(PlacemarkGroup _this);
ProductWriter PlacemarkGroup_getProductWriter(PlacemarkGroup _this);
char* PlacemarkGroup_getDisplayName(PlacemarkGroup _this);
char* PlacemarkGroup_getProductRefString(PlacemarkGroup _this);
dlong PlacemarkGroup_getRawStorageSize1(PlacemarkGroup _this);
void PlacemarkGroup_fireProductNodeChanged1(PlacemarkGroup _this, const char* propertyName);
void PlacemarkGroup_fireProductNodeChanged2(PlacemarkGroup _this, const char* propertyName, Object oldValue, Object newValue);
void PlacemarkGroup_removeFromFile(PlacemarkGroup _this, ProductWriter productWriter);
Object PlacemarkGroup_getExtension(PlacemarkGroup _this, Class arg0);

/* Constants of TiePointGrid */
extern const char* TiePointGrid_PROPERTY_NAME_IMAGE_INFO;
extern const char* TiePointGrid_PROPERTY_NAME_LOG_10_SCALED;
extern const char* TiePointGrid_PROPERTY_NAME_ROI_DEFINITION;
extern const char* TiePointGrid_PROPERTY_NAME_SCALING_FACTOR;
extern const char* TiePointGrid_PROPERTY_NAME_SCALING_OFFSET;
extern const char* TiePointGrid_PROPERTY_NAME_NO_DATA_VALUE;
extern const char* TiePointGrid_PROPERTY_NAME_NO_DATA_VALUE_USED;
extern const char* TiePointGrid_PROPERTY_NAME_VALID_PIXEL_EXPRESSION;
extern const char* TiePointGrid_PROPERTY_NAME_GEOCODING;
extern const char* TiePointGrid_PROPERTY_NAME_STX;
extern const char* TiePointGrid_NO_DATA_TEXT;
extern const char* TiePointGrid_INVALID_POS_TEXT;
extern const char* TiePointGrid_IO_ERROR_TEXT;
extern const char* TiePointGrid_PROPERTY_NAME_DATA;
extern const char* TiePointGrid_PROPERTY_NAME_READ_ONLY;
extern const char* TiePointGrid_PROPERTY_NAME_SYNTHETIC;
extern const char* TiePointGrid_PROPERTY_NAME_UNIT;
extern const char* TiePointGrid_PROPERTY_NAME_NAME;
extern const char* TiePointGrid_PROPERTY_NAME_DESCRIPTION;

/* Functions for class TiePointGrid */

TiePointGrid TiePointGrid_newTiePointGrid1(const char* name, int gridWidth, int gridHeight, float offsetX, float offsetY, float subSamplingX, float subSamplingY, const float* tiePointsElems, int tiePointsLength);
TiePointGrid TiePointGrid_newTiePointGrid2(const char* name, int gridWidth, int gridHeight, float offsetX, float offsetY, float subSamplingX, float subSamplingY, const float* tiePointsElems, int tiePointsLength, int discontinuity);
TiePointGrid TiePointGrid_newTiePointGrid3(const char* name, int gridWidth, int gridHeight, float offsetX, float offsetY, float subSamplingX, float subSamplingY, const float* tiePointsElems, int tiePointsLength, boolean containsAngles);
int TiePointGrid_getDiscontinuity2(const float* tiePointsElems, int tiePointsLength);
int TiePointGrid_getDiscontinuity1(TiePointGrid _this);
void TiePointGrid_setDiscontinuity(TiePointGrid _this, int discontinuity);
boolean TiePointGrid_isFloatingPointType(TiePointGrid _this);
int TiePointGrid_getGeophysicalDataType(TiePointGrid _this);
ProductData TiePointGrid_getSceneRasterData(TiePointGrid _this);
int TiePointGrid_getSceneRasterWidth(TiePointGrid _this);
int TiePointGrid_getSceneRasterHeight(TiePointGrid _this);
float TiePointGrid_getOffsetX(TiePointGrid _this);
float TiePointGrid_getOffsetY(TiePointGrid _this);
float TiePointGrid_getSubSamplingX(TiePointGrid _this);
float TiePointGrid_getSubSamplingY(TiePointGrid _this);
float* TiePointGrid_getTiePoints(TiePointGrid _this, int* _resultArrayLength);
int TiePointGrid_getPixelInt(TiePointGrid _this, int x, int y);
void TiePointGrid_dispose(TiePointGrid _this);
float TiePointGrid_getPixelFloat2(TiePointGrid _this, int x, int y);
float TiePointGrid_getPixelFloat1(TiePointGrid _this, float x, float y);
double TiePointGrid_getPixelDouble(TiePointGrid _this, int x, int y);
void TiePointGrid_setPixelInt(TiePointGrid _this, int x, int y, int pixelValue);
void TiePointGrid_setPixelFloat(TiePointGrid _this, int x, int y, float pixelValue);
void TiePointGrid_setPixelDouble(TiePointGrid _this, int x, int y, double pixelValue);
int* TiePointGrid_getPixels6(TiePointGrid _this, int x, int y, int w, int h, const int* pixelsElems, int pixelsLength, ProgressMonitor pm, int* _resultArrayLength);
float* TiePointGrid_getPixels4(TiePointGrid _this, int x, int y, int w, int h, const float* pixelsElems, int pixelsLength, ProgressMonitor pm, int* _resultArrayLength);
double* TiePointGrid_getPixels2(TiePointGrid _this, int x, int y, int w, int h, const double* pixelsElems, int pixelsLength, ProgressMonitor pm, int* _resultArrayLength);
void TiePointGrid_setPixels3(TiePointGrid _this, int x, int y, int w, int h, const int* pixelsElems, int pixelsLength);
void TiePointGrid_setPixels2(TiePointGrid _this, int x, int y, int w, int h, const float* pixelsElems, int pixelsLength);
void TiePointGrid_setPixels1(TiePointGrid _this, int x, int y, int w, int h, const double* pixelsElems, int pixelsLength);
int* TiePointGrid_readPixels6(TiePointGrid _this, int x, int y, int w, int h, const int* pixelsElems, int pixelsLength, ProgressMonitor pm, int* _resultArrayLength);
float* TiePointGrid_readPixels4(TiePointGrid _this, int x, int y, int w, int h, const float* pixelsElems, int pixelsLength, ProgressMonitor pm, int* _resultArrayLength);
double* TiePointGrid_readPixels2(TiePointGrid _this, int x, int y, int w, int h, const double* pixelsElems, int pixelsLength, ProgressMonitor pm, int* _resultArrayLength);
void TiePointGrid_writePixels6(TiePointGrid _this, int x, int y, int w, int h, const int* pixelsElems, int pixelsLength, ProgressMonitor pm);
void TiePointGrid_writePixels4(TiePointGrid _this, int x, int y, int w, int h, const float* pixelsElems, int pixelsLength, ProgressMonitor pm);
void TiePointGrid_writePixels2(TiePointGrid _this, int x, int y, int w, int h, const double* pixelsElems, int pixelsLength, ProgressMonitor pm);
void TiePointGrid_readRasterData2(TiePointGrid _this, int offsetX, int offsetY, int width, int height, ProductData rasterData, ProgressMonitor pm);
void TiePointGrid_readRasterDataFully2(TiePointGrid _this, ProgressMonitor pm);
void TiePointGrid_writeRasterData2(TiePointGrid _this, int offsetX, int offsetY, int width, int height, ProductData rasterData, ProgressMonitor pm);
void TiePointGrid_writeRasterDataFully2(TiePointGrid _this, ProgressMonitor pm);
void TiePointGrid_acceptVisitor(TiePointGrid _this, ProductVisitor visitor);
TiePointGrid TiePointGrid_cloneTiePointGrid(TiePointGrid _this);
TiePointGrid TiePointGrid_createZenithFromElevationAngleTiePointGrid(TiePointGrid elevationAngleGrid);
TiePointGrid TiePointGrid_createSubset(TiePointGrid sourceTiePointGrid, ProductSubsetDef subsetDef);
int TiePointGrid_getRasterWidth(TiePointGrid _this);
int TiePointGrid_getRasterHeight(TiePointGrid _this);
void TiePointGrid_setModified(TiePointGrid _this, boolean modified);
GeoCoding TiePointGrid_getGeoCoding(TiePointGrid _this);
void TiePointGrid_setGeoCoding(TiePointGrid _this, GeoCoding geoCoding);
Pointing TiePointGrid_getPointing(TiePointGrid _this);
boolean TiePointGrid_canBeOrthorectified(TiePointGrid _this);
double TiePointGrid_getScalingFactor(TiePointGrid _this);
void TiePointGrid_setScalingFactor(TiePointGrid _this, double scalingFactor);
double TiePointGrid_getScalingOffset(TiePointGrid _this);
void TiePointGrid_setScalingOffset(TiePointGrid _this, double scalingOffset);
boolean TiePointGrid_isLog10Scaled(TiePointGrid _this);
void TiePointGrid_setLog10Scaled(TiePointGrid _this, boolean log10Scaled);
boolean TiePointGrid_isScalingApplied(TiePointGrid _this);
boolean TiePointGrid_isValidMaskProperty(const char* propertyName);
boolean TiePointGrid_isNoDataValueSet(TiePointGrid _this);
void TiePointGrid_clearNoDataValue(TiePointGrid _this);
boolean TiePointGrid_isNoDataValueUsed(TiePointGrid _this);
void TiePointGrid_setNoDataValueUsed(TiePointGrid _this, boolean noDataValueUsed);
double TiePointGrid_getNoDataValue(TiePointGrid _this);
void TiePointGrid_setNoDataValue(TiePointGrid _this, double noDataValue);
double TiePointGrid_getGeophysicalNoDataValue(TiePointGrid _this);
void TiePointGrid_setGeophysicalNoDataValue(TiePointGrid _this, double noDataValue);
char* TiePointGrid_getValidPixelExpression(TiePointGrid _this);
void TiePointGrid_setValidPixelExpression(TiePointGrid _this, const char* validPixelExpression);
boolean TiePointGrid_isValidMaskUsed(TiePointGrid _this);
void TiePointGrid_resetValidMask(TiePointGrid _this);
char* TiePointGrid_getValidMaskExpression(TiePointGrid _this);
void TiePointGrid_updateExpression(TiePointGrid _this, const char* oldExternalName, const char* newExternalName);
boolean TiePointGrid_hasRasterData(TiePointGrid _this);
ProductData TiePointGrid_getRasterData(TiePointGrid _this);
void TiePointGrid_setRasterData(TiePointGrid _this, ProductData rasterData);
void TiePointGrid_loadRasterData1(TiePointGrid _this);
void TiePointGrid_loadRasterData2(TiePointGrid _this, ProgressMonitor pm);
void TiePointGrid_unloadRasterData(TiePointGrid _this);
boolean TiePointGrid_isPixelValid2(TiePointGrid _this, int x, int y);
int TiePointGrid_getSampleInt(TiePointGrid _this, int x, int y);
float TiePointGrid_getSampleFloat(TiePointGrid _this, int x, int y);
boolean TiePointGrid_isPixelValid1(TiePointGrid _this, int pixelIndex);
boolean TiePointGrid_isPixelValid3(TiePointGrid _this, int x, int y, ROI roi);
int* TiePointGrid_getPixels5(TiePointGrid _this, int x, int y, int w, int h, const int* pixelsElems, int pixelsLength, int* _resultArrayLength);
float* TiePointGrid_getPixels3(TiePointGrid _this, int x, int y, int w, int h, const float* pixelsElems, int pixelsLength, int* _resultArrayLength);
double* TiePointGrid_getPixels1(TiePointGrid _this, int x, int y, int w, int h, const double* pixelsElems, int pixelsLength, int* _resultArrayLength);
int* TiePointGrid_readPixels5(TiePointGrid _this, int x, int y, int w, int h, const int* pixelsElems, int pixelsLength, int* _resultArrayLength);
float* TiePointGrid_readPixels3(TiePointGrid _this, int x, int y, int w, int h, const float* pixelsElems, int pixelsLength, int* _resultArrayLength);
double* TiePointGrid_readPixels1(TiePointGrid _this, int x, int y, int w, int h, const double* pixelsElems, int pixelsLength, int* _resultArrayLength);
void TiePointGrid_writePixels5(TiePointGrid _this, int x, int y, int w, int h, const int* pixelsElems, int pixelsLength);
void TiePointGrid_writePixels3(TiePointGrid _this, int x, int y, int w, int h, const float* pixelsElems, int pixelsLength);
void TiePointGrid_writePixels1(TiePointGrid _this, int x, int y, int w, int h, const double* pixelsElems, int pixelsLength);
boolean* TiePointGrid_readValidMask(TiePointGrid _this, int x, int y, int w, int h, const boolean* validMaskElems, int validMaskLength, int* _resultArrayLength);
void TiePointGrid_readRasterDataFully1(TiePointGrid _this);
void TiePointGrid_readRasterData1(TiePointGrid _this, int offsetX, int offsetY, int width, int height, ProductData rasterData);
void TiePointGrid_writeRasterDataFully1(TiePointGrid _this);
void TiePointGrid_writeRasterData1(TiePointGrid _this, int offsetX, int offsetY, int width, int height, ProductData rasterData);
ProductData TiePointGrid_createCompatibleRasterData1(TiePointGrid _this);
ProductData TiePointGrid_createCompatibleSceneRasterData(TiePointGrid _this);
ProductData TiePointGrid_createCompatibleRasterData2(TiePointGrid _this, int width, int height);
boolean TiePointGrid_isCompatibleRasterData(TiePointGrid _this, ProductData rasterData, int w, int h);
void TiePointGrid_checkCompatibleRasterData(TiePointGrid _this, ProductData rasterData, int w, int h);
boolean TiePointGrid_hasIntPixels(TiePointGrid _this);
TransectProfileData TiePointGrid_createTransectProfileData(TiePointGrid _this, Shape shape);
ImageInfo TiePointGrid_getImageInfo1(TiePointGrid _this);
void TiePointGrid_setImageInfo(TiePointGrid _this, ImageInfo imageInfo);
void TiePointGrid_fireImageInfoChanged(TiePointGrid _this);
ImageInfo TiePointGrid_getImageInfo2(TiePointGrid _this, ProgressMonitor pm);
ImageInfo TiePointGrid_getImageInfo3(TiePointGrid _this, const double* histoSkipAreasElems, int histoSkipAreasLength, ProgressMonitor pm);
ImageInfo TiePointGrid_createDefaultImageInfo1(TiePointGrid _this, const double* histoSkipAreasElems, int histoSkipAreasLength, ProgressMonitor pm);
ImageInfo TiePointGrid_createDefaultImageInfo2(TiePointGrid _this, const double* histoSkipAreasElems, int histoSkipAreasLength, Histogram histogram);
ProductNodeGroup TiePointGrid_getOverlayMaskGroup(TiePointGrid _this);
BufferedImage TiePointGrid_createColorIndexedImage(TiePointGrid _this, ProgressMonitor pm);
BufferedImage TiePointGrid_createRgbImage(TiePointGrid _this, ProgressMonitor pm);
byte* TiePointGrid_quantizeRasterData1(TiePointGrid _this, double newMin, double newMax, double gamma, ProgressMonitor pm, int* _resultArrayLength);
void TiePointGrid_quantizeRasterData2(TiePointGrid _this, double newMin, double newMax, double gamma, const byte* samplesElems, int samplesLength, int offset, int stride, ProgressMonitor pm);
IndexValidator TiePointGrid_createPixelValidator(TiePointGrid _this, int lineOffset, ROI roi);
double TiePointGrid_scale(TiePointGrid _this, double v);
double TiePointGrid_scaleInverse(TiePointGrid _this, double v);
char* TiePointGrid_getPixelString(TiePointGrid _this, int x, int y);
boolean TiePointGrid_isSourceImageSet(TiePointGrid _this);
MultiLevelImage TiePointGrid_getSourceImage(TiePointGrid _this);
void TiePointGrid_setSourceImage2(TiePointGrid _this, RenderedImage sourceImage);
void TiePointGrid_setSourceImage1(TiePointGrid _this, MultiLevelImage sourceImage);
boolean TiePointGrid_isGeophysicalImageSet(TiePointGrid _this);
MultiLevelImage TiePointGrid_getGeophysicalImage(TiePointGrid _this);
boolean TiePointGrid_isValidMaskImageSet(TiePointGrid _this);
MultiLevelImage TiePointGrid_getValidMaskImage(TiePointGrid _this);
boolean TiePointGrid_isStxSet(TiePointGrid _this);
Stx TiePointGrid_getStx1(TiePointGrid _this);
Stx TiePointGrid_getStx2(TiePointGrid _this, boolean accurate, ProgressMonitor pm);
void TiePointGrid_setStx(TiePointGrid _this, Stx stx);
Shape TiePointGrid_getValidShape(TiePointGrid _this);
ProductNodeGroup TiePointGrid_getRoiMaskGroup(TiePointGrid _this);
int TiePointGrid_getDataType(TiePointGrid _this);
dlong TiePointGrid_getNumDataElems(TiePointGrid _this);
void TiePointGrid_setData(TiePointGrid _this, ProductData data);
ProductData TiePointGrid_getData(TiePointGrid _this);
void TiePointGrid_setDataElems(TiePointGrid _this, Object elems);
Object TiePointGrid_getDataElems(TiePointGrid _this);
int TiePointGrid_getDataElemSize(TiePointGrid _this);
void TiePointGrid_setReadOnly(TiePointGrid _this, boolean readOnly);
boolean TiePointGrid_isReadOnly(TiePointGrid _this);
void TiePointGrid_setUnit(TiePointGrid _this, const char* unit);
char* TiePointGrid_getUnit(TiePointGrid _this);
boolean TiePointGrid_isSynthetic(TiePointGrid _this);
void TiePointGrid_setSynthetic(TiePointGrid _this, boolean synthetic);
void TiePointGrid_fireProductNodeDataChanged(TiePointGrid _this);
dlong TiePointGrid_getRawStorageSize2(TiePointGrid _this, ProductSubsetDef subsetDef);
ProductData TiePointGrid_createCompatibleProductData(TiePointGrid _this, int numElems);
ProductNode TiePointGrid_getOwner(TiePointGrid _this);
char* TiePointGrid_getName(TiePointGrid _this);
void TiePointGrid_setName(TiePointGrid _this, const char* name);
char* TiePointGrid_getDescription(TiePointGrid _this);
void TiePointGrid_setDescription(TiePointGrid _this, const char* description);
boolean TiePointGrid_isModified(TiePointGrid _this);
char* TiePointGrid_toString(TiePointGrid _this);
boolean TiePointGrid_isValidNodeName(const char* name);
Product TiePointGrid_getProduct(TiePointGrid _this);
ProductReader TiePointGrid_getProductReader(TiePointGrid _this);
ProductWriter TiePointGrid_getProductWriter(TiePointGrid _this);
char* TiePointGrid_getDisplayName(TiePointGrid _this);
char* TiePointGrid_getProductRefString(TiePointGrid _this);
dlong TiePointGrid_getRawStorageSize1(TiePointGrid _this);
void TiePointGrid_fireProductNodeChanged1(TiePointGrid _this, const char* propertyName);
void TiePointGrid_fireProductNodeChanged2(TiePointGrid _this, const char* propertyName, Object oldValue, Object newValue);
void TiePointGrid_removeFromFile(TiePointGrid _this, ProductWriter productWriter);
Object TiePointGrid_getExtension(TiePointGrid _this, Class arg0);

/* Functions for class AngularDirection */

AngularDirection AngularDirection_newAngularDirection(double azimuth, double zenith);
boolean AngularDirection_equals(AngularDirection _this, Object obj);
char* AngularDirection_toString(AngularDirection _this);

/* Constants of FlagCoding */
extern const char* FlagCoding_PROPERTY_NAME_NAME;
extern const char* FlagCoding_PROPERTY_NAME_DESCRIPTION;

/* Functions for class FlagCoding */

FlagCoding FlagCoding_newFlagCoding(const char* name);
MetadataAttribute FlagCoding_getFlag(FlagCoding _this, const char* name);
char** FlagCoding_getFlagNames(FlagCoding _this, int* _resultArrayLength);
MetadataAttribute FlagCoding_addFlag(FlagCoding _this, const char* name, int flagMask, const char* description);
int FlagCoding_getFlagMask(FlagCoding _this, const char* name);
void FlagCoding_acceptVisitor(FlagCoding _this, ProductVisitor visitor);
void FlagCoding_addElement(FlagCoding _this, MetadataElement element);
void FlagCoding_addAttribute(FlagCoding _this, MetadataAttribute attribute);
MetadataAttribute FlagCoding_addSample(FlagCoding _this, const char* name, int value, const char* description);
int FlagCoding_getSampleCount(FlagCoding _this);
char* FlagCoding_getSampleName(FlagCoding _this, int index);
int FlagCoding_getSampleValue(FlagCoding _this, int index);
ProductNodeGroup FlagCoding_getElementGroup(FlagCoding _this);
MetadataElement FlagCoding_getParentElement(FlagCoding _this);
void FlagCoding_addElementAt(FlagCoding _this, MetadataElement element, int index);
boolean FlagCoding_removeElement(FlagCoding _this, MetadataElement element);
int FlagCoding_getNumElements(FlagCoding _this);
MetadataElement FlagCoding_getElementAt(FlagCoding _this, int index);
char** FlagCoding_getElementNames(FlagCoding _this, int* _resultArrayLength);
MetadataElement* FlagCoding_getElements(FlagCoding _this, int* _resultArrayLength);
MetadataElement FlagCoding_getElement(FlagCoding _this, const char* name);
boolean FlagCoding_containsElement(FlagCoding _this, const char* name);
int FlagCoding_getElementIndex(FlagCoding _this, MetadataElement element);
boolean FlagCoding_removeAttribute(FlagCoding _this, MetadataAttribute attribute);
int FlagCoding_getNumAttributes(FlagCoding _this);
MetadataAttribute FlagCoding_getAttributeAt(FlagCoding _this, int index);
char** FlagCoding_getAttributeNames(FlagCoding _this, int* _resultArrayLength);
MetadataAttribute* FlagCoding_getAttributes(FlagCoding _this, int* _resultArrayLength);
MetadataAttribute FlagCoding_getAttribute(FlagCoding _this, const char* name);
boolean FlagCoding_containsAttribute(FlagCoding _this, const char* name);
int FlagCoding_getAttributeIndex(FlagCoding _this, MetadataAttribute attribute);
double FlagCoding_getAttributeDouble(FlagCoding _this, const char* name, double defaultValue);
ProductData_UTC FlagCoding_getAttributeUTC(FlagCoding _this, const char* name, ProductData_UTC defaultValue);
int FlagCoding_getAttributeInt(FlagCoding _this, const char* name, int defaultValue);
void FlagCoding_setAttributeInt(FlagCoding _this, const char* name, int value);
void FlagCoding_setAttributeDouble(FlagCoding _this, const char* name, double value);
void FlagCoding_setAttributeUTC(FlagCoding _this, const char* name, ProductData_UTC value);
char* FlagCoding_getAttributeString(FlagCoding _this, const char* name, const char* defaultValue);
void FlagCoding_setAttributeString(FlagCoding _this, const char* name, const char* value);
void FlagCoding_setModified(FlagCoding _this, boolean modified);
MetadataElement FlagCoding_createDeepClone(FlagCoding _this);
void FlagCoding_dispose(FlagCoding _this);
ProductNode FlagCoding_getOwner(FlagCoding _this);
char* FlagCoding_getName(FlagCoding _this);
void FlagCoding_setName(FlagCoding _this, const char* name);
char* FlagCoding_getDescription(FlagCoding _this);
void FlagCoding_setDescription(FlagCoding _this, const char* description);
boolean FlagCoding_isModified(FlagCoding _this);
char* FlagCoding_toString(FlagCoding _this);
boolean FlagCoding_isValidNodeName(const char* name);
Product FlagCoding_getProduct(FlagCoding _this);
ProductReader FlagCoding_getProductReader(FlagCoding _this);
ProductWriter FlagCoding_getProductWriter(FlagCoding _this);
char* FlagCoding_getDisplayName(FlagCoding _this);
char* FlagCoding_getProductRefString(FlagCoding _this);
void FlagCoding_updateExpression(FlagCoding _this, const char* oldExternalName, const char* newExternalName);
void FlagCoding_removeFromFile(FlagCoding _this, ProductWriter productWriter);
Object FlagCoding_getExtension(FlagCoding _this, Class arg0);

/* Functions for class Map */

int Map_size(Map _this);
boolean Map_isEmpty(Map _this);
boolean Map_containsKey(Map _this, Object arg0);
boolean Map_containsValue(Map _this, Object arg0);
Object Map_get(Map _this, Object arg0);
Object Map_put(Map _this, Object arg0, Object arg1);
Object Map_remove(Map _this, Object arg0);
void Map_putAll(Map _this, Map arg0);
void Map_clear(Map _this);
Set Map_keySet(Map _this);
Collection Map_values(Map _this);
Set Map_entrySet(Map _this);
boolean Map_equals(Map _this, Object arg0);
int Map_hashCode(Map _this);

/* Functions for class ProductReader */

ProductReaderPlugIn ProductReader_getReaderPlugIn(ProductReader _this);
Object ProductReader_getInput(ProductReader _this);
ProductSubsetDef ProductReader_getSubsetDef(ProductReader _this);
Product ProductReader_readProductNodes(ProductReader _this, Object input, ProductSubsetDef subsetDef);
void ProductReader_readBandRasterData(ProductReader _this, Band destBand, int destOffsetX, int destOffsetY, int destWidth, int destHeight, ProductData destBuffer, ProgressMonitor pm);
void ProductReader_close(ProductReader _this);

/* Functions for class RGBChannelDef */

RGBChannelDef RGBChannelDef_newRGBChannelDef();
char* RGBChannelDef_getSourceName(RGBChannelDef _this, int index);
void RGBChannelDef_setSourceName(RGBChannelDef _this, int index, const char* sourceName);
char** RGBChannelDef_getSourceNames(RGBChannelDef _this, int* _resultArrayLength);
void RGBChannelDef_setSourceNames(RGBChannelDef _this, const char** bandNamesElems, int bandNamesLength);
boolean RGBChannelDef_isAlphaUsed(RGBChannelDef _this);
boolean RGBChannelDef_isGammaUsed(RGBChannelDef _this, int index);
double RGBChannelDef_getGamma(RGBChannelDef _this, int index);
void RGBChannelDef_setGamma(RGBChannelDef _this, int index, double gamma);
double RGBChannelDef_getMinDisplaySample(RGBChannelDef _this, int index);
void RGBChannelDef_setMinDisplaySample(RGBChannelDef _this, int index, double min);
double RGBChannelDef_getMaxDisplaySample(RGBChannelDef _this, int index);
void RGBChannelDef_setMaxDisplaySample(RGBChannelDef _this, int index, double max);
Object RGBChannelDef_clone(RGBChannelDef _this);

/* Constants of ProductData */
extern const int ProductData_TYPE_UNDEFINED;
extern const int ProductData_TYPE_INT8;
extern const int ProductData_TYPE_INT16;
extern const int ProductData_TYPE_INT32;
extern const int ProductData_TYPE_UINT8;
extern const int ProductData_TYPE_UINT16;
extern const int ProductData_TYPE_UINT32;
extern const int ProductData_TYPE_FLOAT32;
extern const int ProductData_TYPE_FLOAT64;
extern const int ProductData_TYPE_ASCII;
extern const int ProductData_TYPE_UTC;
extern const char* ProductData_TYPESTRING_INT8;
extern const char* ProductData_TYPESTRING_INT16;
extern const char* ProductData_TYPESTRING_INT32;
extern const char* ProductData_TYPESTRING_UINT8;
extern const char* ProductData_TYPESTRING_UINT16;
extern const char* ProductData_TYPESTRING_UINT32;
extern const char* ProductData_TYPESTRING_FLOAT32;
extern const char* ProductData_TYPESTRING_FLOAT64;
extern const char* ProductData_TYPESTRING_ASCII;
extern const char* ProductData_TYPESTRING_UTC;

/* Functions for class ProductData */

ProductData ProductData_createInstance1(int type);
ProductData ProductData_createInstance2(int type, int numElems);
ProductData ProductData_createInstance3(int type, Object data);
ProductData ProductData_createInstance5(const byte* elemsElems, int elemsLength);
ProductData ProductData_createUnsignedInstance1(const byte* elemsElems, int elemsLength);
ProductData ProductData_createInstance10(const short* elemsElems, int elemsLength);
ProductData ProductData_createUnsignedInstance3(const short* elemsElems, int elemsLength);
ProductData ProductData_createInstance8(const int* elemsElems, int elemsLength);
ProductData ProductData_createUnsignedInstance2(const int* elemsElems, int elemsLength);
ProductData ProductData_createInstance9(const dlong* elemsElems, int elemsLength);
ProductData ProductData_createInstance4(const char* strData);
ProductData ProductData_createInstance7(const float* elemsElems, int elemsLength);
ProductData ProductData_createInstance6(const double* elemsElems, int elemsLength);
int ProductData_getType1(ProductData _this);
int ProductData_getElemSize2(int type);
int ProductData_getElemSize1(ProductData _this);
char* ProductData_getTypeString2(int type);
int ProductData_getType2(const char* type);
char* ProductData_getTypeString1(ProductData _this);
boolean ProductData_isInt(ProductData _this);
boolean ProductData_isIntType(int type);
boolean ProductData_isSigned(ProductData _this);
boolean ProductData_isUnsigned(ProductData _this);
boolean ProductData_isUIntType(int type);
boolean ProductData_isFloatingPointType(int type);
boolean ProductData_isScalar(ProductData _this);
int ProductData_getNumElems(ProductData _this);
int ProductData_getElemInt(ProductData _this);
dlong ProductData_getElemUInt(ProductData _this);
float ProductData_getElemFloat(ProductData _this);
double ProductData_getElemDouble(ProductData _this);
char* ProductData_getElemString(ProductData _this);
boolean ProductData_getElemBoolean(ProductData _this);
int ProductData_getElemIntAt(ProductData _this, int index);
dlong ProductData_getElemUIntAt(ProductData _this, int index);
float ProductData_getElemFloatAt(ProductData _this, int index);
double ProductData_getElemDoubleAt(ProductData _this, int index);
char* ProductData_getElemStringAt(ProductData _this, int index);
boolean ProductData_getElemBooleanAt(ProductData _this, int index);
void ProductData_setElemInt(ProductData _this, int value);
void ProductData_setElemUInt(ProductData _this, dlong value);
void ProductData_setElemFloat(ProductData _this, float value);
void ProductData_setElemDouble(ProductData _this, double value);
void ProductData_setElemString(ProductData _this, const char* value);
void ProductData_setElemBoolean(ProductData _this, boolean value);
void ProductData_setElemIntAt(ProductData _this, int index, int value);
void ProductData_setElemUIntAt(ProductData _this, int index, dlong value);
void ProductData_setElemFloatAt(ProductData _this, int index, float value);
void ProductData_setElemDoubleAt(ProductData _this, int index, double value);
void ProductData_setElemStringAt(ProductData _this, int index, const char* value);
void ProductData_setElemBooleanAt(ProductData _this, int index, boolean value);
Object ProductData_getElems(ProductData _this);
void ProductData_setElems(ProductData _this, Object data);
void ProductData_readFrom4(ProductData _this, ImageInputStream input);
void ProductData_readFrom3(ProductData _this, int pos, ImageInputStream input);
void ProductData_readFrom1(ProductData _this, int startPos, int numElems, ImageInputStream input);
void ProductData_readFrom2(ProductData _this, int startPos, int numElems, ImageInputStream input, dlong inputPos);
void ProductData_writeTo4(ProductData _this, ImageOutputStream output);
void ProductData_writeTo3(ProductData _this, int pos, ImageOutputStream output);
void ProductData_writeTo1(ProductData _this, int startPos, int numElems, ImageOutputStream output);
void ProductData_writeTo2(ProductData _this, int startPos, int numElems, ImageOutputStream output, dlong outputPos);
char* ProductData_toString(ProductData _this);
int ProductData_hashCode(ProductData _this);
boolean ProductData_equals(ProductData _this, Object other);
boolean ProductData_equalElems(ProductData _this, ProductData other);
void ProductData_dispose(ProductData _this);

/* Functions for class GeoPos */

GeoPos GeoPos_newGeoPos(float lat, float lon);
float GeoPos_getLat(GeoPos _this);
float GeoPos_getLon(GeoPos _this);
void GeoPos_setLocation(GeoPos _this, float lat, float lon);
boolean GeoPos_isValid(GeoPos _this);
boolean GeoPos_areValid(const GeoPos aElems, int aLength);
void GeoPos_setInvalid(GeoPos _this);
boolean GeoPos_equals(GeoPos _this, Object obj);
int GeoPos_hashCode(GeoPos _this);
char* GeoPos_toString(GeoPos _this);
void GeoPos_normalize(GeoPos _this);
float GeoPos_normalizeLon(float lon);
char* GeoPos_getLatString(GeoPos _this);
char* GeoPos_getLonString(GeoPos _this);

/* Constants of ProductNodeGroup */
extern const char* ProductNodeGroup_PROPERTY_NAME_NAME;
extern const char* ProductNodeGroup_PROPERTY_NAME_DESCRIPTION;

/* Functions for class ProductNodeGroup */

ProductNodeGroup ProductNodeGroup_newProductNodeGroup(const char* name);
boolean ProductNodeGroup_isTakingOverNodeOwnership(ProductNodeGroup _this);
int ProductNodeGroup_getNodeCount(ProductNodeGroup _this);
ProductNode ProductNodeGroup_getAt(ProductNodeGroup _this, int index);
char** ProductNodeGroup_getNodeDisplayNames(ProductNodeGroup _this, int* _resultArrayLength);
char** ProductNodeGroup_getNodeNames(ProductNodeGroup _this, int* _resultArrayLength);
int ProductNodeGroup_indexOfName(ProductNodeGroup _this, const char* name);
int ProductNodeGroup_indexOf(ProductNodeGroup _this, ProductNode element);
ProductNode ProductNodeGroup_getByDisplayName(ProductNodeGroup _this, const char* displayName);
ProductNode ProductNodeGroup_get(ProductNodeGroup _this, const char* name);
boolean ProductNodeGroup_containsName(ProductNodeGroup _this, const char* name);
boolean ProductNodeGroup_contains(ProductNodeGroup _this, ProductNode node);
boolean ProductNodeGroup_add(ProductNodeGroup _this, ProductNode node);
void ProductNodeGroup_addAt(ProductNodeGroup _this, int index, ProductNode node);
boolean ProductNodeGroup_remove(ProductNodeGroup _this, ProductNode node);
void ProductNodeGroup_removeAll(ProductNodeGroup _this);
void ProductNodeGroup_clearRemovedList(ProductNodeGroup _this);
Collection ProductNodeGroup_getRemovedNodes(ProductNodeGroup _this);
void ProductNodeGroup_setModified(ProductNodeGroup _this, boolean modified);
void ProductNodeGroup_acceptVisitor(ProductNodeGroup _this, ProductVisitor visitor);
void ProductNodeGroup_dispose(ProductNodeGroup _this);
void ProductNodeGroup_updateExpression(ProductNodeGroup _this, const char* oldExternalName, const char* newExternalName);
ProductNode ProductNodeGroup_getOwner(ProductNodeGroup _this);
char* ProductNodeGroup_getName(ProductNodeGroup _this);
void ProductNodeGroup_setName(ProductNodeGroup _this, const char* name);
char* ProductNodeGroup_getDescription(ProductNodeGroup _this);
void ProductNodeGroup_setDescription(ProductNodeGroup _this, const char* description);
boolean ProductNodeGroup_isModified(ProductNodeGroup _this);
char* ProductNodeGroup_toString(ProductNodeGroup _this);
boolean ProductNodeGroup_isValidNodeName(const char* name);
Product ProductNodeGroup_getProduct(ProductNodeGroup _this);
ProductReader ProductNodeGroup_getProductReader(ProductNodeGroup _this);
ProductWriter ProductNodeGroup_getProductWriter(ProductNodeGroup _this);
char* ProductNodeGroup_getDisplayName(ProductNodeGroup _this);
char* ProductNodeGroup_getProductRefString(ProductNodeGroup _this);
void ProductNodeGroup_removeFromFile(ProductNodeGroup _this, ProductWriter productWriter);
Object ProductNodeGroup_getExtension(ProductNodeGroup _this, Class arg0);

/* Functions for class ProductUtils */

ProductUtils ProductUtils_newProductUtils();
ImageInfo ProductUtils_createImageInfo(const RasterDataNode rastersElems, int rastersLength, boolean assignMissingImageInfos, ProgressMonitor pm);
BufferedImage ProductUtils_createRgbImage(const RasterDataNode rastersElems, int rastersLength, ImageInfo imageInfo, ProgressMonitor pm);
BufferedImage ProductUtils_createColorIndexedImage(RasterDataNode rasterDataNode, ProgressMonitor pm);
MapInfo ProductUtils_createSuitableMapInfo1(Product product, Rectangle rect, MapProjection mapProjection);
MapInfo ProductUtils_createSuitableMapInfo2(Product product, MapProjection mapProjection, double orientation, double noDataValue);
Dimension ProductUtils_getOutputRasterSize(Product product, Rectangle rect, MapTransform mapTransform, double pixelSizeX, double pixelSizeY);
Point2D* ProductUtils_createMapEnvelope2(Product product, Rectangle rect, MapTransform mapTransform, int* _resultArrayLength);
Point2D* ProductUtils_createMapEnvelope1(Product product, Rectangle rect, int step, MapTransform mapTransform, int* _resultArrayLength);
Point2D* ProductUtils_getMinMax(const Point2D boundaryElems, int boundaryLength, int* _resultArrayLength);
Point2D* ProductUtils_createMapBoundary(Product product, Rectangle rect, int step, MapTransform mapTransform, int* _resultArrayLength);
GeoPos* ProductUtils_createGeoBoundary1(Product product, int step, int* _resultArrayLength);
GeoPos* ProductUtils_createGeoBoundary2(Product product, Rectangle region, int step, int* _resultArrayLength);
GeoPos* ProductUtils_createGeoBoundary3(Product product, Rectangle region, int step, boolean usePixelCenter, int* _resultArrayLength);
GeoPos* ProductUtils_createGeoBoundary4(RasterDataNode raster, Rectangle region, int step, int* _resultArrayLength);
GeneralPath* ProductUtils_createGeoBoundaryPaths1(Product product, int* _resultArrayLength);
GeneralPath* ProductUtils_createGeoBoundaryPaths2(Product product, Rectangle region, int step, int* _resultArrayLength);
GeneralPath* ProductUtils_createGeoBoundaryPaths3(Product product, Rectangle region, int step, boolean usePixelCenter, int* _resultArrayLength);
PixelPos* ProductUtils_createPixelBoundary1(Product product, Rectangle rect, int step, int* _resultArrayLength);
PixelPos* ProductUtils_createPixelBoundary2(Product product, Rectangle rect, int step, boolean usePixelCenter, int* _resultArrayLength);
PixelPos* ProductUtils_createPixelBoundary3(RasterDataNode raster, Rectangle rect, int step, int* _resultArrayLength);
PixelPos* ProductUtils_createRectBoundary1(Rectangle rect, int step, int* _resultArrayLength);
PixelPos* ProductUtils_createRectBoundary2(Rectangle rect, int step, boolean usePixelCenter, int* _resultArrayLength);
void ProductUtils_copyFlagCodings(Product source, Product target);
FlagCoding ProductUtils_copyFlagCoding(FlagCoding sourceFlagCoding, Product target);
IndexCoding ProductUtils_copyIndexCoding(IndexCoding sourceIndexCoding, Product target);
void ProductUtils_copyMasks(Product sourceProduct, Product targetProduct);
void ProductUtils_copyOverlayMasks(Product sourceProduct, Product targetProduct);
void ProductUtils_copyRoiMasks(Product sourceProduct, Product targetProduct);
void ProductUtils_copyFlagBands2(Product sourceProduct, Product targetProduct, boolean copySourceImage);
void ProductUtils_copyFlagBands1(Product sourceProduct, Product targetProduct);
TiePointGrid ProductUtils_copyTiePointGrid(const char* gridName, Product sourceProduct, Product targetProduct);
Band ProductUtils_copyBand4(const char* sourceBandName, Product sourceProduct, Product targetProduct, boolean copySourceImage);
Band ProductUtils_copyBand2(const char* sourceBandName, Product sourceProduct, const char* targetBandName, Product targetProduct, boolean copySourceImage);
void ProductUtils_copyRasterDataNodeProperties(RasterDataNode sourceRaster, RasterDataNode targetRaster);
Band ProductUtils_copyBand3(const char* sourceBandName, Product sourceProduct, Product targetProduct);
Band ProductUtils_copyBand1(const char* sourceBandName, Product sourceProduct, const char* targetBandName, Product targetProduct);
void ProductUtils_copySpectralBandProperties(Band sourceBand, Band targetBand);
void ProductUtils_copyGeoCoding(Product sourceProduct, Product targetProduct);
void ProductUtils_copyTiePointGrids(Product sourceProduct, Product targetProduct);
void ProductUtils_copyVectorData(Product sourceProduct, Product targetProduct);
boolean ProductUtils_canGetPixelPos1(Product product);
boolean ProductUtils_canGetPixelPos2(RasterDataNode raster);
BufferedImage ProductUtils_createDensityPlotImage(RasterDataNode raster1, float sampleMin1, float sampleMax1, RasterDataNode raster2, float sampleMin2, float sampleMax2, Mask roiMask, int width, int height, Color background, BufferedImage image, ProgressMonitor pm);
BufferedImage ProductUtils_overlayMasks(RasterDataNode raster, BufferedImage overlayBIm, ProgressMonitor pm);
GeoPos ProductUtils_getCenterGeoPos(Product product);
int ProductUtils_normalizeGeoPolygon(const GeoPos polygonElems, int polygonLength);
int ProductUtils_normalizeGeoPolygon_old(const GeoPos polygonElems, int polygonLength);
void ProductUtils_denormalizeGeoPolygon(const GeoPos polygonElems, int polygonLength);
void ProductUtils_denormalizeGeoPos(GeoPos geoPos);
void ProductUtils_denormalizeGeoPos_old(GeoPos geoPos);
int ProductUtils_getRotationDirection(const GeoPos polygonElems, int polygonLength);
double ProductUtils_getAngleSum(const GeoPos polygonElems, int polygonLength);
GeneralPath ProductUtils_convertToPixelPath(GeneralPath geoPath, GeoCoding geoCoding);
GeneralPath ProductUtils_convertToGeoPath(Shape shape, GeoCoding geoCoding);
void ProductUtils_copyMetadata2(Product source, Product target);
void ProductUtils_copyMetadata1(MetadataElement source, MetadataElement target);
void ProductUtils_copyPreferredTileSize(Product sourceProduct, Product targetProduct);
GeoTIFFMetadata ProductUtils_createGeoTIFFMetadata2(Product product);
GeoTIFFMetadata ProductUtils_createGeoTIFFMetadata1(GeoCoding geoCoding, int width, int height);
GeneralPath ProductUtils_areaToPath(Area negativeArea, double deltaX);
void ProductUtils_addElementToHistory(Product product, MetadataElement elem);
char** ProductUtils_removeInvalidExpressions(Product product, int* _resultArrayLength);
char* ProductUtils_findSuitableQuicklookBandName(Product product);
PixelPos* ProductUtils_computeSourcePixelCoordinates(GeoCoding sourceGeoCoding, int sourceWidth, int sourceHeight, GeoCoding destGeoCoding, Rectangle destArea, int* _resultArrayLength);
float* ProductUtils_computeMinMaxY(const PixelPos pixelPositionsElems, int pixelPositionsLength, int* _resultArrayLength);
void ProductUtils_copyBandsForGeomTransform1(Product sourceProduct, Product targetProduct, double defaultNoDataValue, Map addedRasterDataNodes);
void ProductUtils_copyBandsForGeomTransform2(Product sourceProduct, Product targetProduct, boolean includeTiePointGrids, double defaultNoDataValue, Map targetToSourceMap);
ProductData_UTC ProductUtils_getScanLineTime(Product product, double y);
double ProductUtils_getGeophysicalSampleDouble(Band band, int pixelX, int pixelY, int level);
dlong ProductUtils_getGeophysicalSampleLong(Band band, int pixelX, int pixelY, int level);

/* Constants of MetadataAttribute */
extern const char* MetadataAttribute_PROPERTY_NAME_DATA;
extern const char* MetadataAttribute_PROPERTY_NAME_READ_ONLY;
extern const char* MetadataAttribute_PROPERTY_NAME_SYNTHETIC;
extern const char* MetadataAttribute_PROPERTY_NAME_UNIT;
extern const char* MetadataAttribute_PROPERTY_NAME_NAME;
extern const char* MetadataAttribute_PROPERTY_NAME_DESCRIPTION;

/* Functions for class MetadataAttribute */

MetadataAttribute MetadataAttribute_newMetadataAttribute(const char* name, ProductData data, boolean readOnly);
MetadataElement MetadataAttribute_getParentElement(MetadataAttribute _this);
boolean MetadataAttribute_equals(MetadataAttribute _this, Object object);
void MetadataAttribute_acceptVisitor(MetadataAttribute _this, ProductVisitor visitor);
MetadataAttribute MetadataAttribute_createDeepClone(MetadataAttribute _this);
int MetadataAttribute_getDataType(MetadataAttribute _this);
boolean MetadataAttribute_isFloatingPointType(MetadataAttribute _this);
dlong MetadataAttribute_getNumDataElems(MetadataAttribute _this);
void MetadataAttribute_setData(MetadataAttribute _this, ProductData data);
ProductData MetadataAttribute_getData(MetadataAttribute _this);
void MetadataAttribute_setDataElems(MetadataAttribute _this, Object elems);
Object MetadataAttribute_getDataElems(MetadataAttribute _this);
int MetadataAttribute_getDataElemSize(MetadataAttribute _this);
void MetadataAttribute_setReadOnly(MetadataAttribute _this, boolean readOnly);
boolean MetadataAttribute_isReadOnly(MetadataAttribute _this);
void MetadataAttribute_setUnit(MetadataAttribute _this, const char* unit);
char* MetadataAttribute_getUnit(MetadataAttribute _this);
boolean MetadataAttribute_isSynthetic(MetadataAttribute _this);
void MetadataAttribute_setSynthetic(MetadataAttribute _this, boolean synthetic);
void MetadataAttribute_fireProductNodeDataChanged(MetadataAttribute _this);
void MetadataAttribute_dispose(MetadataAttribute _this);
ProductData MetadataAttribute_createCompatibleProductData(MetadataAttribute _this, int numElems);
ProductNode MetadataAttribute_getOwner(MetadataAttribute _this);
char* MetadataAttribute_getName(MetadataAttribute _this);
void MetadataAttribute_setName(MetadataAttribute _this, const char* name);
char* MetadataAttribute_getDescription(MetadataAttribute _this);
void MetadataAttribute_setDescription(MetadataAttribute _this, const char* description);
boolean MetadataAttribute_isModified(MetadataAttribute _this);
void MetadataAttribute_setModified(MetadataAttribute _this, boolean modified);
char* MetadataAttribute_toString(MetadataAttribute _this);
boolean MetadataAttribute_isValidNodeName(const char* name);
Product MetadataAttribute_getProduct(MetadataAttribute _this);
ProductReader MetadataAttribute_getProductReader(MetadataAttribute _this);
ProductWriter MetadataAttribute_getProductWriter(MetadataAttribute _this);
char* MetadataAttribute_getDisplayName(MetadataAttribute _this);
char* MetadataAttribute_getProductRefString(MetadataAttribute _this);
void MetadataAttribute_updateExpression(MetadataAttribute _this, const char* oldExternalName, const char* newExternalName);
void MetadataAttribute_removeFromFile(MetadataAttribute _this, ProductWriter productWriter);
Object MetadataAttribute_getExtension(MetadataAttribute _this, Class arg0);

#ifdef __cplusplus
} /* extern "C" */
#endif
#endif /* !BEAM_CAPI_H */