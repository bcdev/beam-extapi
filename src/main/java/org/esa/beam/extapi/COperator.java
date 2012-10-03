package org.esa.beam.extapi;

import com.bc.ceres.core.ProgressMonitor;
import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.gpf.*;
import org.esa.beam.framework.gpf.annotations.SourceProduct;
import org.esa.beam.framework.gpf.annotations.TargetProduct;

import java.awt.*;
import java.util.Map;

/**
 * @author Norman Fomferra
 */
public class COperator extends Operator {
    static {
        System.loadLibrary("beam-capi");
    }


    private final int opId;

    @SourceProduct
    Product sourceProduct;

    @TargetProduct
    Product targetProduct;

    public COperator(int opId) {
        this.opId = opId;
    }

    @Override
    public void initialize() throws OperatorException {
        initializeNative(opId);
    }

    @Override
    public void dispose() {
        super.dispose();
        disposeNative(opId);
    }

    @Override
    public void computeTile(Band targetBand, Tile targetTile, ProgressMonitor pm) throws OperatorException {
        pm.beginTask("Computing tile", 1);
        Rectangle targetRectangle = targetTile.getRectangle();
        computeTileNative(opId,
                          getTargetProduct().getBandIndex(targetBand.getName()),
                          targetRectangle.x,
                          targetRectangle.y,
                          targetRectangle.width,
                          targetRectangle.height,
                          targetTile.getDataBuffer().getElems());
        pm.done();
    }

    @Override
    public void computeTileStack(Map<Band, Tile> targetTiles, Rectangle targetRectangle, ProgressMonitor pm) throws OperatorException {
        int[] bandIndexes = new int[targetTiles.size()];
        Object[] tileData = new Object[targetTiles.size()];
        int i = 0;
        for (Map.Entry<Band, Tile> bandTileEntry : targetTiles.entrySet()) {
            bandIndexes[i] = getTargetProduct().getBandIndex(bandTileEntry.getKey().getName());
            tileData[i] = bandTileEntry.getValue().getDataBuffer().getElems();
            i++;
        }
        pm.beginTask("Computing tile stack", 1);
        computeTileStackNative(opId,
                               bandIndexes,
                               targetRectangle.x,
                               targetRectangle.y,
                               targetRectangle.width,
                               targetRectangle.height,
                               tileData);
        pm.done();
    }

    private native void computeTileNative(int opId, int bandIndex, int x, int y, int width, int height, Object tileData);

    private native void computeTileStackNative(int opId, int[] bandIndexes, int x, int y, int width, int height, Object[] tileData);

    private native void initializeNative(int opId);

    private native void disposeNative(int opId);

    public static void registerNativeOperator(final int opId, String opName, String opDescription) {
        OperatorSpiRegistry registry = GPF.getDefaultInstance().getOperatorSpiRegistry();
        OperatorSpi operatorSpi = new OperatorSpi(COperator.class, opName) {
            @Override
            public Operator createOperator() throws OperatorException {
                return new COperator(opId);
            }
        };
        registry.addOperatorSpi(operatorSpi);
    }
}
