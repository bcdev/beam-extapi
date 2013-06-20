#include "Python.h"
#include "beampy_carray.h"


/*
 * Default implementation for the 'free_fn' field.
 */
void CArray_FreeMemory(void* items, int length)
{
    if (items != NULL) {
        free(items);
    }
}

/*
 * Helper for the __init__() method for CArray_Type
 */
static void CArray_InitInstance(CArray* self, const char* format, void* items, size_t item_size, int length, CArrayFree free_fn)
{
    self->format[0] = format[0];
    self->format[1] = 0;
    self->items = items;
    self->item_size = item_size;
    self->length = length;
    self->free_fn = free_fn;
    self->num_exports = 0;
}

/*
 * Implements the CArray() constructor for CArray_Type
 */
static PyObject* CArray_new(PyTypeObject* type, PyObject* args, PyObject* kwds)
{
    CArray* self;
    printf("CArray_new\n");
    self = (CArray*) type->tp_alloc(type, 0);
    if (self != NULL) {
        CArray_InitInstance(self, "\0", 0, 0, 0, 0);
    }
    return (PyObject*) self;
}

/*
 * Helper for the __init__() method for CArray_Type
 */
size_t CArray_ItemSize(const char* format)
{
    if (strcmp(format, "b") == 0 || strcmp(format, "B") == 0) {
        return sizeof (char);
    } else if (strcmp(format, "h") == 0 || strcmp(format, "H") == 0) {
        return sizeof (short);
    } else if (strcmp(format, "i") == 0 || strcmp(format, "I") == 0) {
        return sizeof (int);
    } else if (strcmp(format, "l") == 0 || strcmp(format, "L") == 0) {
        return sizeof (long);
    } else if (strcmp(format, "f") == 0) {
        return sizeof (float);
    } else if (strcmp(format, "d") == 0) {
        return sizeof (double);
    } else {
        PyErr_SetString(PyExc_ValueError, "format must be one of (b, B, h, H, i, I, l, L, f, d)");
        return 0;
    }
}

/*
 * Implements the __init__() method for CArray_Type
 */
static int CArray_init(CArray* self, PyObject* args, PyObject* kwds)
{
    const char* format = NULL;
    void* items = NULL;
    int length = 0;
    size_t item_size = 0;

    printf("CArray_init\n");

    if (!PyArg_ParseTuple(args, "si", &format, &length)) {
        return 1;
    }

    item_size = CArray_ItemSize(format);
    if (item_size <= 0) {
        return 2;
    }

    if (length <= 0) {
        PyErr_SetString(PyExc_ValueError, "length must be > 0");
        return 3;
    }

    items = calloc(item_size, length);
    if (items == NULL) {
        PyErr_SetString(PyExc_MemoryError, "failed to allocate memory for CArray");
        return 4;
    }

    CArray_InitInstance(self, format, items, item_size, length, CArray_FreeMemory);
    return 0;
}

/*
 * Implements the dealloc() method for CArray_Type
 */
static void CArray_dealloc(CArray* self)
{
    printf("CArray_dealloc\n");
    if (self->items != NULL && self->free_fn != NULL) {
        self->free_fn(self->items, self->length);
        self->items = NULL;
    }
    Py_TYPE(self)->tp_free((PyObject*) self);
}


/*
 * Implements the repr() method for CArray_Type
 */
static PyObject* CArray_repr(CArray* self)
{
    return PyUnicode_FromFormat("CArray('%s', %d)", self->format, self->length);
}

/*
 * Implements all specific methods of the CArray_Type (none so far)
 */
static PyMethodDef CArray_methods[] = {
    {NULL} 
};

#define PRINT_FLAG(F) printf("CArray_getbufferproc: %s = %d\n", #F, (flags & F) != 0);
#define PRINT_MEMB(F, M) printf("CArray_getbufferproc: %s = " ## F ## "\n", #M, M);

/*
 * Implements the getbuffer() method of the <buffer> interface for CArray_Type
 */
int CArray_getbufferproc(CArray* self, Py_buffer* view, int flags)
{
    int ret = 0;

    /*
    printf("CArray_getbufferproc\n");
    PRINT_FLAG(PyBUF_ANY_CONTIGUOUS);
    PRINT_FLAG(PyBUF_CONTIG);
    PRINT_FLAG(PyBUF_CONTIG_RO);
    PRINT_FLAG(PyBUF_C_CONTIGUOUS);
    PRINT_FLAG(PyBUF_FORMAT);
    PRINT_FLAG(PyBUF_FULL);
    PRINT_FLAG(PyBUF_FULL_RO);
    PRINT_FLAG(PyBUF_F_CONTIGUOUS);
    PRINT_FLAG(PyBUF_INDIRECT);
    PRINT_FLAG(PyBUF_ND);
    PRINT_FLAG(PyBUF_READ);
    PRINT_FLAG(PyBUF_RECORDS);
    PRINT_FLAG(PyBUF_RECORDS_RO);
    PRINT_FLAG(PyBUF_SIMPLE);
    PRINT_FLAG(PyBUF_STRIDED);
    PRINT_FLAG(PyBUF_STRIDED_RO);
    PRINT_FLAG(PyBUF_STRIDES);
    PRINT_FLAG(PyBUF_WRITE);
    PRINT_FLAG(PyBUF_WRITEABLE);
    */

    // According to Python documentation,
    // buffer allocation shall be done in the 5 following steps;

    // Step 1/5
    if (self->items == NULL) {
        view->obj = NULL;
        PyErr_SetString(PyExc_BufferError, "invalid CArray, items == NULL");
        return -1;
    }

    // Step 2/5
    view->buf = self->items;
    view->len = self->length * self->item_size;
    view->itemsize = self->item_size;
    view->readonly = 0;
    view->ndim = 1;
    view->shape = (Py_ssize_t*) malloc(view->ndim* sizeof (Py_ssize_t));
    view->shape[0] = self->length;
    view->strides = (Py_ssize_t*) malloc(view->ndim* sizeof (Py_ssize_t));
    view->strides[0] = self->item_size;
    view->suboffsets = NULL;
    if ((flags & PyBUF_FORMAT) != 0) {
        view->format = self->format;
    } else {
        view->format = NULL;
    }
    /*
    PRINT_MEMB("%d", view->len);
    PRINT_MEMB("%d", view->ndim);
    PRINT_MEMB("%s", view->format);
    PRINT_MEMB("%d", view->itemsize);
    PRINT_MEMB("%d", view->readonly);
    PRINT_MEMB("%p", view->shape);
    if (view->shape != NULL)
        PRINT_MEMB("%d", view->shape[0]);
    PRINT_MEMB("%p", view->strides);
    if (view->strides != NULL)
        PRINT_MEMB("%d", view->strides[0]);
    PRINT_MEMB("%p", view->suboffsets);
    if (view->suboffsets != NULL)
        PRINT_MEMB("%d", view->suboffsets[0]);
    */

    // Step 3/5
    self->num_exports++;

    // Step 4/5
    view->obj = (PyObject*) self;
    Py_INCREF(view->obj);

    // Step 5/5
    return ret;
}

/*
 * Implements the releasebuffer() method of the <buffer> interface for CArray_Type
 */
void CArray_releasebufferproc(CArray* self, Py_buffer* view)
{
    printf("CArray_releasebufferproc\n");
    // Step 1
    self->num_exports--;
    // Step 2
    if (self->num_exports == 0) {
        view->buf = NULL;
        if (view->strides != NULL) {
            free(view->strides);
            view->strides = NULL;
        }
        if (view->strides != NULL) {
            free(view->strides);
            view->shape = NULL;
        }
        // todo: release resources...
    }
}

/*
 * Implements <buffer> interface for CArray_Type
 */
static PyBufferProcs CArray_as_buffer = {
    (getbufferproc) CArray_getbufferproc,
    (releasebufferproc) CArray_releasebufferproc
};

/*
 * Implements the length method of the <sequence> interface for CArray_Type
 */
Py_ssize_t CArray_sq_length(CArray* self)
{
    return self->length;
}

/*
 * Implements the item getter method of the <sequence> interface for CArray_Type
 */
PyObject* CArray_sq_item(CArray* self, Py_ssize_t index)
{
    if (index < 0) {
        index += self->length;
    }
    if (index < 0 || index >= self->length) {
        PyErr_SetString(PyExc_IndexError, "CArray index out of bounds");
        return NULL;
    }
    switch (*self->format) {
    case 'b':
    case 'B':
        return  PyLong_FromLong(((char*) self->items)[index]);
    case 'h':
    case 'H':
        return  PyLong_FromLong(((short*) self->items)[index]);
    case 'i':
    case 'I':
        return  PyLong_FromLong(((int*) self->items)[index]);
    case 'f':
        return  PyFloat_FromDouble(((float*) self->items)[index]);
    case 'd':
        return  PyFloat_FromDouble(((double*) self->items)[index]);
    default:
        PyErr_SetString(PyExc_ValueError, "format must be one of (b, B, h, H, i, I, l, L, f, d)");
        return NULL;
    }
}

/*
 * Implements the item assignment method of the <sequence> interface for CArray_Type
 */
int CArray_sq_ass_item(CArray* self, Py_ssize_t index, PyObject* other)
{
    if (index < 0) {
        index += self->length;
    }
    if (index < 0 || index >= self->length) {
        PyErr_SetString(PyExc_IndexError, "CArray index out of bounds");
        return -1;
    }
    switch (*self->format) {
    case 'b':
    case 'B':
        ((char*) self->items)[index] = (char) PyLong_AsLong(other);
        return 0;
    case 'h':
    case 'H':
        ((short*) self->items)[index] = (short) PyLong_AsLong(other);
        return 0;
    case 'i':
    case 'I':
        ((int*) self->items)[index] = (int) PyLong_AsLong(other);
        return 0;
    case 'f':
        ((float*) self->items)[index] = (float) PyFloat_AsDouble(other);
        return 0;
    case 'd':
        ((double*) self->items)[index] = PyFloat_AsDouble(other);
        return 0;
    default:
        PyErr_SetString(PyExc_ValueError, "format must be one of (b, B, h, H, i, I, l, L, f, d)");
        return -1;
    }
}

/*
 * Implements the <sequence> interface for CArray_Type
 */
static PySequenceMethods CArray_as_sequence = {
    (lenfunc) CArray_sq_length,            /* sq_length */ 
    NULL,   /* sq_concat */
    NULL,   /* sq_repeat */
    (ssizeargfunc) CArray_sq_item,         /* sq_item */
    NULL,   /* was_sq_slice */
    (ssizeobjargproc) CArray_sq_ass_item,  /* sq_ass_item */
    NULL,   /* was_sq_ass_slice */
    NULL,   /* sq_contains */
    NULL,   /* sq_inplace_concat */
    NULL,   /* sq_inplace_repeat */
};

#define CARRAY_DOC  "C-array object, a light-weight wrapper around one-dimensional C-arrays."

/*
 * Implements the new CArray_Type
 */
PyTypeObject CArray_Type = {
    PyVarObject_HEAD_INIT(NULL, 0)
    "${libName}.CArray",       /* tp_name */
    sizeof(CArray),         /* tp_basicsize */
    0,                         /* tp_itemsize */
    (destructor)CArray_dealloc,/* tp_dealloc */
    0,                         /* tp_print */
    0,                         /* tp_getattr */
    0,                         /* tp_setattr */
    0,                         /* tp_reserved */
    (reprfunc)CArray_repr,     /* tp_repr */
    0,                         /* tp_as_number */
    &CArray_as_sequence,       /* tp_as_sequence */
    0,                         /* tp_as_mapping */
    0,                         /* tp_hash  */
    0,                         /* tp_call */
    0,                         /* tp_str */
    0,                         /* tp_getattro */
    0,                         /* tp_setattro */
    &CArray_as_buffer,         /* tp_as_buffer */
    Py_TPFLAGS_DEFAULT,        /* tp_flags */
    CARRAY_DOC,                /* tp_doc */
    0,                         /* tp_traverse */
    0,                         /* tp_clear */
    0,                         /* tp_richcompare */
    0,                         /* tp_weaklistoffset */
    0,                         /* tp_iter */
    0,                         /* tp_iternext */
    CArray_methods,            /* tp_methods */
    0,                         /* tp_members */
    0,                         /* tp_getset */
    0,                         /* tp_base */
    0,                         /* tp_dict */
    0,                         /* tp_descr_get */
    0,                         /* tp_descr_set */
    0,                         /* tp_dictoffset */
    (initproc)CArray_init,     /* tp_init */
    0,                         /* tp_alloc */
    CArray_new,                /* tp_new */
};

/**
 * Factory function for a CArray used if the item's memory is already given.
 *
 * Note that this method does not increment the reference counter. You are responsible for 
 * calling Py_INCREF(obj) in the returned obj yourself
 */
PyObject* CArray_FromMemory(const char* format, void* items, int length, CArrayFree free_fn) {
    PyTypeObject* type = &CArray_Type;
    CArray* self;
    size_t item_size;

    item_size = CArray_ItemSize(format);
    if (item_size <= 0) {
        return NULL;
    }

    if (items == NULL) {
        PyErr_SetString(PyExc_MemoryError, "no element data given");
        return NULL;
    }

    if (length <= 0) {
        PyErr_SetString(PyExc_ValueError, "length must be > 0");
        return NULL;
    }

    self = (CArray*) type->tp_alloc(type, 0);
    CArray_InitInstance(self, format, items, item_size, length, free_fn);
    return (PyObject*) self;
}

/**
 * Factory function for a CArray used if only the number of items is known given.
 *
 * Note that this method does not increment the reference counter. You are responsible for
 * calling Py_INCREF(obj) in the returned obj yourself
 */
PyObject* CArray_New(const char* format, int length) {
    PyTypeObject* type = &CArray_Type;
    CArray* self;
    void* items;
    size_t item_size;

    item_size = CArray_ItemSize(format);
    if (item_size <= 0) {
        return NULL;
    }

    items = calloc(item_size, length);
    if (items == NULL) {
        PyErr_SetString(PyExc_MemoryError, "out of memory");
        return NULL;
    }

    if (length <= 0) {
        PyErr_SetString(PyExc_ValueError, "length must be > 0");
        return NULL;
    }

    self = (CArray*) type->tp_alloc(type, 0);
    CArray_InitInstance(self, format, items, item_size, length, CArray_FreeMemory);
    return (PyObject*) self;
}


