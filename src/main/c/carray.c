#include <Python.h>
#include "structmember.h"


extern PyTypeObject CArray_type;

typedef struct {
    PyObject_HEAD
	char type_code[2];
	int length;
	int elem_size;
    void* elems;
	int num_exports;
} CArrayObj;

static void CArray_dealloc(CArrayObj* self)
{
	printf("CArray_dealloc\n");
	if (self->elems != NULL) {
		free(self->elems);
		self->elems = NULL;
	}
    Py_TYPE(self)->tp_free((PyObject*) self);
}

static PyObject* CArray_new(PyTypeObject* type, PyObject* args, PyObject* kwds)
{
    CArrayObj* self;
	printf("CArray_new\n");
    self = (CArrayObj*) type->tp_alloc(type, 0);
    if (self != NULL) {
		self->type_code[0] = 
		self->type_code[1] = 0;
        self->length = 0;
        self->elem_size = 0;
        self->elems = 0;
		self->num_exports = 0;
    }
    return (PyObject*) self;
}

static int CArray_init(CArrayObj* self, PyObject* args, PyObject* kwds)
{
	const char* type_code = NULL;
	int length = 0;
	int elem_size = 0;

	printf("CArray_init\n");
    if (!PyArg_ParseTuple(args, "si", &type_code, &length)) {
        return -1;
	}

	if (length <= 0) {
        PyErr_SetString(PyExc_ValueError, "length must be > 0");
        return 1;
	}

    if (strcmp(type_code, "B") == 0) {
		elem_size = sizeof (char);
    } else if (strcmp(type_code, "S") == 0) {
		elem_size = sizeof (short);
    } else if (strcmp(type_code, "I") == 0) {
		elem_size = sizeof (int);
    } else if (strcmp(type_code, "F") == 0) {
		elem_size = sizeof (float);
    } else if (strcmp(type_code, "D") == 0) {
		elem_size = sizeof (double);
    } else {
        PyErr_SetString(PyExc_ValueError, "type_code must be one of 'B', 'S', 'I', 'F', 'D'");
        return 1;
    }
	
	self->type_code[0] = type_code[0];
	self->elem_size = elem_size;
	self->length = length;
	self->elems = calloc(elem_size, length);
	if (self->elems == NULL) {
        PyErr_SetString(PyExc_MemoryError, "elems");
        return 1;
	}

    return 0;
}

static PyObject* CArray_noargs(CArrayObj* self)
{
	printf("CArray_noargs self=%p\n", self);
    return Py_BuildValue("");
}

static PyObject* CArray_varargs(CArrayObj* self, PyObject* args)
{
	printf("CArray_varargs self=%p, args=%p\n", self, args);
    return Py_BuildValue("");
}

static PyObject* CArray_varargsstatic(CArrayObj* self, PyObject* args)
{
	printf("CArray_varargsstatic self=%p, args=%p\n", self, args);
    return Py_BuildValue("");
}

static PyObject* CArray_varargsclass(PyTypeObject* cls, PyObject* args)
{
	printf("CArray_varargsclass cls=%p, args=%p\n", cls, args);
    return Py_BuildValue("");
}


static PyMethodDef CArray_methods[] = {
    {"noargs", (PyCFunction) CArray_noargs, METH_NOARGS, "METH_NOARGS test"},
    {"varargs", (PyCFunction) CArray_varargs, METH_VARARGS, "METH_VARARGS test"},
    {"varargsstatic", (PyCFunction) CArray_varargsstatic, METH_VARARGS | METH_STATIC, "METH_VARARGS | METH_STATIC test"},
    {"varargsclass", (PyCFunction) CArray_varargsclass, METH_VARARGS | METH_CLASS, "METH_VARARGS | METH_CLASS test"},
    {NULL}  /* Sentinel */
};

#define PRINT_FLAG(F) printf("CArray_getbufferproc: %s = %d\n", #F, (flags & F) != 0);
#define PRINT_MEMB(F, M) printf("CArray_getbufferproc: %s = " ## F ## "\n", #M, M);

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
	// Step 1
	if (self->elems == NULL) {
		view->obj = NULL;
		PyErr_SetString(PyExc_BufferError, "elems");
		return -1;
	}
	// Step 2
	
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
	// Step 3
	self->num_exports++;
	// Step 4
	view->obj = (PyObject*) self;
	Py_INCREF(view->obj);
	// Step 5
	return ret;
}

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

static PyBufferProcs CArray_as_buffer = {
	(getbufferproc) CArray_getbufferproc,
	(releasebufferproc) CArray_releasebufferproc
};

Py_ssize_t CArray_sq_length(CArrayObj* self)
{
	return self->length;
}

PyObject* CArray_sq_item(CArrayObj* self, Py_ssize_t index)
{
	if (index < 0) {
		index += self->length;
	}
	if (index < 0 || index >= self->length) {
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
		PyErr_SetString(PyExc_ValueError, "type_code must be one of 'B', 'S', 'I', 'F', 'D'");
		return NULL;
	}
}

int CArray_sq_ass_item(CArrayObj* self, Py_ssize_t index, PyObject* other)
{
	if (index < 0) {
		index += self->length;
	}
	if (index < 0 || index >= self->length) {
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
		PyErr_SetString(PyExc_ValueError, "type_code must be one of 'B', 'S', 'I', 'F', 'D'");
		return -1;
	}
}

static PyObject* CArray_repr(CArrayObj* self)
{
	return PyUnicode_FromFormat("CArray('%s', %d)", self->type_code, self->length);
}

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


static PyTypeObject CArray_type = {
    PyVarObject_HEAD_INIT(NULL, 0)
    "carray.CArray",           /* tp_name */
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
    "C-Array Object",          /* tp_doc */
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

	
static PyModuleDef CArray_module = {
    PyModuleDef_HEAD_INIT,
    "carray",
    "Provides a light-weight wrapper around one-dimensional C-arrays.",
    -1,
    NULL, NULL, NULL, NULL, NULL
};


PyMODINIT_FUNC PyInit_carray() 
{
    PyObject* m;

    //CArray_type.tp_new = PyType_GenericNew;
    if (PyType_Ready(&CArray_type) < 0) {
        return NULL;
	}

    m = PyModule_Create(&CArray_module);
    if (m == NULL) {
        return NULL;
	}

    Py_INCREF(&CArray_type);
    PyModule_AddObject(m, "CArray", (PyObject*) &CArray_type);
    return m;
}
