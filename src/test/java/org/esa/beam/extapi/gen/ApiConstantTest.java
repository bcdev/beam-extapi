package org.esa.beam.extapi.gen;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Type;
import org.junit.Assert;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

/**
 * @author Norman Fomferra
 */
public class ApiConstantTest {

    @Test
    public void testConstants() {
        ClassDoc classDoc = DocMock.createClassDoc(File.class);
        FieldDoc fieldDoc = getFieldDoc(classDoc, "pathSeparator");
        ApiClass apiClass = new ApiClass(classDoc);
        ApiConstant apiConstant = new ApiConstant(apiClass, fieldDoc);
        assertSame(apiClass, apiConstant.getEnclosingClass());
        assertSame(fieldDoc, apiConstant.getFieldDoc());
        assertEquals("pathSeparator", apiConstant.getJavaName());
        assertEquals("Ljava/lang/String;", apiConstant.getJavaSignature());
        assertEquals("String", apiConstant.getType().typeName());
        assertEquals("java.io.File#pathSeparator:Ljava/lang/String;", apiConstant.toString());

    }

    private static FieldDoc getFieldDoc(ClassDoc classDoc, String otherName) {
        FieldDoc[] fields = classDoc.fields();
        for (int i = 0; i < fields.length; i++) {
            FieldDoc field = fields[i];
            String name = field.name();
            if (name.equals(otherName)) {
                    return field;
            }
        }
        return null;
    }
}
