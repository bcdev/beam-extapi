typedef void (*CArrayFree)(void* array_elems, int array_length);


/*
 * Represents an instance of the CArray_Type class
 */
typedef struct {
    PyObject_HEAD
	char type_code[2];
	int length;
	int elem_size;
    void* elems;
    CArrayFree free_fn;
	size_t num_exports;
} CArrayObj;

/*
 * Default implementation for the 'free_fn' field.
 */
static void CArray_releaseElements(void* elems, int length)
{
	if (elems != NULL) {
	    free(elems);
	}
}

/*
 * Helper for the __init__() method for CArray_Type
 */
static void CArray_initInstance(CArrayObj* self, const char* type_code, void* elems, int elem_size, int length, CArrayFree free_fn)
{
	self->type_code[0] = type_code[0];
	self->type_code[1] = 0;
	self->elems = elems;
	self->elem_size = elem_size;
	self->length = length;
	self->free_fn = free_fn;
	self->num_exports = 0;
}

/*
 * Implements the CArray() constructor for CArray_Type
 */
static PyObject* CArray_new(PyTypeObject* type, PyObject* args, PyObject* kwds)
{
    CArrayObj* self;
	printf("CArray_new\n");
    self = (CArrayObj*) type->tp_alloc(type, 0);
    if (self != NULL) {
        CArray_initInstance(self, "\0", 0, 0, 0, 0);
    }
    return (PyObject*) self;
}

/*
 * Helper for the __init__() method for CArray_Type
 */
static int CArray_getElemSize(const char* type_code)
{
    if (strcmp(type_code, "B") == 0) {
		return sizeof (char);
    } else if (strcmp(type_code, "S") == 0) {
		return sizeof (short);
    } else if (strcmp(type_code, "I") == 0) {
		return sizeof (int);
    } else if (strcmp(type_code, "F") == 0) {
		return sizeof (float);
    } else if (strcmp(type_code, "D") == 0) {
		return sizeof (double);
    } else {
        PyErr_SetString(PyExc_ValueError, "type_code must be one of 'B', 'S', 'I', 'F', 'D'");
        return 0;
    }
}

/*
 * Implements the __init__() method for CArray_Type
 */
static int CArray_init(CArrayObj* self, PyObject* args, PyObject* kwds)
{
	const char* type_code = NULL;
	void* elems = NULL;
	int length = 0;
	int elem_size = 0;

	printf("CArray_init\n");

    if (!PyArg_ParseTuple(args, "si", &type_code, &length)) {
        return 1;
	}

	elem_size = CArray_getElemSize(type_code);
	if (elem_size <= 0) {
	    return 2;
	}

	if (length <= 0) {
        PyErr_SetString(PyExc_ValueError, "length must be > 0");
        return 3;
	}

    elems = calloc(elem_size, length);
    if (elems == NULL) {
        PyErr_SetString(PyExc_MemoryError, "failed to allocate memory for CArray");
        return 4;
    }

    CArray_initInstance(self, type_code, elems, elem_size, length, CArray_releaseElements);
    return 0;
}

/*
 * Implements the dealloc() method for CArray_Type
 */
static void CArray_dealloc(CArrayObj* self)
{
	printf("CArray_dealloc\n");
	if (self->elems != NULL && self->free_fn != NULL) {
		self->free_fn(self->elems, self->length);
		self->elems = NULL;
	}
    Py_TYPE(self)->tp_free((PyObject*) self);
}


/*
 * Implements the repr() method for CArray_Type
 */
static PyObject* CArray_repr(CArrayObj* self)
{
	return PyUnicode_FromFormat("CArray('%s', %d)", self->type_code, self->length);
}

/*
 * A test instance method of CArray_Type
 */
static PyObject* CArray_noargs(CArrayObj* self)
{
	printf("CArray_noargs self=%p\n", self);
    return Py_BuildValue("");
}

/*
 * A test instance method of CArray_Type
 */
static PyObject* CArray_varargs(CArrayObj* self, PyObject* args)
{
	printf("CArray_varargs self=%p, args=%p\n", self, args);
    return Py_BuildValue("");
}

/*
 * A test static method of CArray_Type
 */
static PyObject* CArray_varargsstatic(CArrayObj* self, PyObject* args)
{
	printf("CArray_varargsstatic self=%p, args=%p\n", self, args);
    return Py_BuildValue("");
}

/*
 * A test class method of CArray_Type
 */
static PyObject* CArray_varargsclass(PyTypeObject* cls, PyObject* args)
{
	printf("CArray_varargsclass cls=%p, args=%p\n", cls, args);
    return Py_BuildValue("");
}


/*
 * Implements all specific methods of the CArray_Type (currently all methods are tests)
 */
static PyMethodDef CArray_methods[] = {
    {"noargs", (PyCFunction) CArray_noargs, METH_NOARGS, "METH_NOARGS test"},
    {"varargs", (PyCFunction) CArray_varargs, METH_VARARGS, "METH_VARARGS test"},
    {"varargsstatic", (PyCFunction) CArray_varargsstatic, METH_VARARGS | METH_STATIC, "METH_VARARGS | METH_STATIC test"},
    {"varargsclass", (PyCFunction) CArray_varargsclass, METH_VARARGS | METH_CLASS, "METH_VARARGS | METH_CLASS test"},
    {NULL}  /* Sentinel */
};

#define PRINT_FLAG(F) printf("CArray_getbufferproc: %s = %d\n", #F, (flags & F) != 0);
#define PRINT_MEMB(F, M) printf("CArray_getbufferproc: %s = " ## F ## "\n", #M, M);

/*
 * Implements the getbuffer() method of the <buffer> interface for CArray_Type
 */
int CArray_getbufferproc(CArrayObj* self, Py_buffer* view, int flags)
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
	if (self->elems == NULL) {
		view->obj = NULL;
		PyErr_SetString(PyExc_BufferError, "invalid CArray, elems == NULL");
		return -1;
	}

	// Step 2/5
	view->buf = self->elems;
	view->len = self->length * self->elem_size;
	view->itemsize = self->elem_size;
	view->readonly = 0;
	view->ndim = 1;
	view->shape = (Py_ssize_t*) malloc(view->ndim* sizeof (Py_ssize_t));
	view->shape[0] = self->length;
	view->strides = (Py_ssize_t*) malloc(view->ndim* sizeof (Py_ssize_t));
	view->strides[0] = self->elem_size;
	view->suboffsets = NULL;
	if ((flags & PyBUF_FORMAT) != 0) {
		view->format = self->type_code;
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
void CArray_releasebufferproc(CArrayObj* self, Py_buffer* view)
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
Py_ssize_t CArray_sq_length(CArrayObj* self)
{
	return self->length;
}

/*
 * Implements the item getter method of the <sequence> interface for CArray_Type
 */
PyObject* CArray_sq_item(CArrayObj* self, Py_ssize_t index)
{
	if (index < 0) {
		index += self->length;
	}
	if (index < 0 || index >= self->length) {
	    PyErr_SetString(PyExc_IndexError, "CArray index out of bounds");
		return NULL;
	}
	switch (*self->type_code) {
	case 'B':
		return  PyLong_FromLong(((char*) self->elems)[index]);
	case 'S':
		return  PyLong_FromLong(((short*) self->elems)[index]);
	case 'I':
		return  PyLong_FromLong(((int*) self->elems)[index]);
	case 'F':
		return  PyFloat_FromDouble(((float*) self->elems)[index]);
	case 'D':
		return  PyFloat_FromDouble(((double*) self->elems)[index]);
	default:
		PyErr_SetString(PyExc_ValueError, "CArray type_code must be one of 'B', 'S', 'I', 'F', 'D'");
		return NULL;
	}
}

/*
 * Implements the item assignment method of the <sequence> interface for CArray_Type
 */
int CArray_sq_ass_item(CArrayObj* self, Py_ssize_t index, PyObject* other)
{
	if (index < 0) {
		index += self->length;
	}
	if (index < 0 || index >= self->length) {
	    PyErr_SetString(PyExc_IndexError, "CArray index out of bounds");
		return -1;
	}
	switch (*self->type_code) {
	case 'B':
		((char*) self->elems)[index] = (char) PyLong_AsLong(other);
		return 0;
	case 'S':
		((short*) self->elems)[index] = (short) PyLong_AsLong(other);
		return 0;
	case 'I':
		((int*) self->elems)[index] = (int) PyLong_AsLong(other);
		return 0;
	case 'F':
		((float*) self->elems)[index] = (float) PyFloat_AsDouble(other);
		return 0;
	case 'D':
		((double*) self->elems)[index] = PyFloat_AsDouble(other);
		return 0;
	default:
		PyErr_SetString(PyExc_ValueError, "CArray type_code must be one of 'B', 'S', 'I', 'F', 'D'");
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
extern PyTypeObject CArray_Type = {
    PyVarObject_HEAD_INIT(NULL, 0)
    "${libName}.CArray",       /* tp_name */
    sizeof(CArrayObj),         /* tp_basicsize */
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

/*
 * The following CArray factory function will be used whenever a new C-Array
 * is created by the BEAM C-API and returned to Python.
 *
 * Note that this method already increment the reference counter on the returned object.
 */
PyObject* CArray_createInstance(const char* type_code, void* elems, int length, CArrayFree free_fn) {
	PyTypeObject* type = &CArray_Type;
	CArrayObj* self;
	int elem_size;

	elem_size = CArray_getElemSize(type_code);
	if (elem_size <= 0) {
	    return NULL;
	}

    if (elems == NULL) {
        PyErr_SetString(PyExc_MemoryError, "no element data given");
        return NULL;
    }

	if (length <= 0) {
        PyErr_SetString(PyExc_ValueError, "length must be > 0");
        return NULL;
	}

    self = (CArrayObj*) type->tp_alloc(type, 0);
    CArray_initInstance(self, type_code, elems, elem_size, length, free_fn);
    Py_INCREF(self);
    return (PyObject*) self;
}

/*
 * Implements the 'carray' module.
 * May be used for testing this CArray_Type in a separate module.
 * However, the _beampy.pyd library already registers it.
 */
/*
static PyModuleDef CArray_Module = {
    PyModuleDef_HEAD_INIT,
    "carray",
    "Provides a light-weight wrapper around one-dimensional C-arrays.",
    -1,
    NULL, NULL, NULL, NULL, NULL
};
*/

/*
 * May be used for testing this CArray_Type in a separate module.
 * However, the _beampy.pyd library already registers it.
 */
/*
PyMODINIT_FUNC PyInit_carray()
{
    PyObject* m;

    if (PyType_Ready(&CArray_Type) < 0) {
        return NULL;
	}

    m = PyModule_Create(&CArray_Module);
    if (m == NULL) {
        return NULL;
	}

    Py_INCREF(&CArray_Type);
    PyModule_AddObject(m, "CArray", (PyObject*) &CArray_Type);
    return m;
}
*/