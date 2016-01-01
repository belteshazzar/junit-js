
function fail(msg) {
	org.junit.Assert.fail(msg);
}

function assertEquals(msg,a,b) {
	if (b===undefined) {
		b = a;
		a = msg;
		if (isInt(a) && isInt(b)) org.junit.Assert["assertEquals(long,long)"](a,b);
		else if (isFloat(a) && isFloat(b)) org.junit.Assert["assertEquals(double,double)"](a,b);
		else org.junit.Assert["assertEquals(Object,Object)"](a,b);
	} else {
		if (isInt(a) && isInt(b)) org.junit.Assert["assertEquals(String,long,long)"](msg,a,b);
		else if (isFloat(a) && isFloat(b)) org.junit.Assert["assertEquals(String,double,double)"](msg,a,b);
		else org.junit.Assert["assertEquals(String,Object,Object)"](msg,a,b);
	}
}

function assertDefined(a) {
	org.junit.Assert.assertTrue(a!==undefined);
}

function assertTrue(a,b) {
	if (b===undefined) {
		org.junit.Assert.assertTrue(a);
	} else {
		org.junit.Assert.assertTrue(a,b);
	}
}
