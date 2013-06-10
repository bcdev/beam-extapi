#ifndef BEAM_CAPI_J_H
#define BEAM_CAPI_J_H

#ifdef __cplusplus
extern "C" {
#endif

#include "jni.h"

int beam_init_api();
JavaVM* beam_getJavaVM();
JNIEnv* beam_getJNIEnv();

/* java.lang classes. */
extern jclass classBoolean;
extern jclass classByte;
extern jclass classCharacter;
extern jclass classShort;
extern jclass classInteger;
extern jclass classLong;
extern jclass classFloat;
extern jclass classDouble;
extern jclass classString;

/* java.util classes. */
extern jclass classHashMap;
extern jclass classHashSet;

/* API classes. */
extern jclass classGeoCoding;
extern jclass classProductWriter;
extern jclass classGPF;
extern jclass classIndexCoding;
extern jclass classPixelPos;
extern jclass classProductIO;
extern jclass classPlacemark;
extern jclass classMetadataElement;
extern jclass classProduct;
extern jclass classColorPaletteDef;
extern jclass classImageInfo;
extern jclass classProductManager;
extern jclass classImageGeometry;
extern jclass classBand;
extern jclass classPlacemarkGroup;
extern jclass classTiePointGrid;
extern jclass classAngularDirection;
extern jclass classFlagCoding;
extern jclass classProductReader;
extern jclass classRGBChannelDef;
extern jclass classProductData;
extern jclass classGeoPos;
extern jclass classProductNodeGroup;
extern jclass classProductUtils;
extern jclass classMetadataAttribute;

/* Used non-API classes. */
extern jclass classProgressMonitor;
extern jclass classMultiLevelImage;
extern jclass classParser;
extern jclass classTerm;
extern jclass classWritableNamespace;
extern jclass classColor;
extern jclass classDimension;
extern jclass classRectangle;
extern jclass classRenderingHints;
extern jclass classRenderingHints_Key;
extern jclass classShape;
extern jclass classAffineTransform;
extern jclass classArea;
extern jclass classGeneralPath;
extern jclass classPoint2D;
extern jclass classBufferedImage;
extern jclass classComponentColorModel;
extern jclass classIndexColorModel;
extern jclass classRenderedImage;
extern jclass classFile;
extern jclass classClass;
extern jclass classObject;
extern jclass classCollection;
extern jclass classIterator;
extern jclass classMap;
extern jclass classImageInputStream;
extern jclass classImageOutputStream;
extern jclass classROI;
extern jclass classProductReaderPlugIn;
extern jclass classProductSubsetDef;
extern jclass classProductWriterPlugIn;
extern jclass classBitmaskDef;
extern jclass classColorPaletteDef_Point;
extern jclass classImageInfo_HistogramMatching;
extern jclass classMask;
extern jclass classPlacemarkDescriptor;
extern jclass classPointing;
extern jclass classPointingFactory;
extern jclass classProduct_AutoGrouping;
extern jclass classProductData_UTC;
extern jclass classProductManager_Listener;
extern jclass classProductNode;
extern jclass classProductNodeListener;
extern jclass classProductVisitor;
extern jclass classRasterDataNode;
extern jclass classSampleCoding;
extern jclass classScaling;
extern jclass classStx;
extern jclass classTransectProfileData;
extern jclass classVectorDataNode;
extern jclass classDatum;
extern jclass classMapInfo;
extern jclass classMapProjection;
extern jclass classMapTransform;
extern jclass classOperator;
extern jclass classOperatorSpiRegistry;
extern jclass classBitRaster;
extern jclass classGeoTIFFMetadata;
extern jclass classHistogram;
extern jclass classIndexValidator;
extern jclass classSimpleFeature;
extern jclass classSimpleFeatureType;
extern jclass classCoordinateReferenceSystem;
extern jclass classMathTransform;


#ifdef __cplusplus
} /* extern "C" */
#endif
#endif /* !BEAM_CAPI_J_H */