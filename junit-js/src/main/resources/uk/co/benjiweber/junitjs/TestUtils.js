
function isInt(i) {
	return Number(i) == i && i % 1 == 0;
}

function isFloat(f) {
	return Number(f) == f && f % 1 != 0;
}

function display(o) {
	displaySomething(o,0);
}

function displaySomething(o,i) {
	if (o instanceof Array) {
		displayArray(o,i);
	} else if (typeof(o) === "object") {
		displayObject(o,i);
	} else {
		print(indent(i) + o + " [" + typeof(o) + "]");
	}
}

function indent(i) {
	var r = "";
	for (var x=0 ; x<i ; x++) {
		r += "  ";
	}
	return r;
}

function displayObject(o,i) {
	print(indent(i) + "[object]");
	for (var key in o) {
		if (o.hasOwnProperty(key)) {
			print(indent(i+1) + key + ": ");
			displaySomething(o[key],i+2);
		}
	}
}

function displayArray(a,i) {
	print(indent(i) + "[array]");
	for (var x=0 ; x<a.length ; x++) {
		print(indent(i+1) + "[" + x + "]: ");
		displaySomething(a[x],i+2);
	}
}
