
function fail(msg) {
	org.junit.Assert.fail(msg);
}

function isInt(i) {
	return Number(i) == i && i % 1 == 0;
}

function isFloat(f) {
	return Number(f) == f && f % 1 != 0;
}

function assertEquals(msg,a,b) {
	if (!b) {
		b = a;
		a = msg;
		msg = "";
	}

	if (isInt(a) && isInt(b)) org.junit.Assert["assertEquals(String,long,long)"](msg,a,b);
	else if (isFloat(a) && isFloat(b)) org.junit.Assert["assertEquals(String,double,double)"](msg,a,b);
	else if (typeof a == "string" && typeof b == "string") {
		org.junit.Assert["assertEquals(String,String,String)"](msg,a,b);
	} else {
		org.junit.Assert["assertEquals(String,Object,Object)"](msg,a,b);
	} 
}