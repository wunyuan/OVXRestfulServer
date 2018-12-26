package org.nchc.restService;

import org.nchc.onos.vpls.OpennsaRestHandlerForOnosVpls;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

class OpennsaApplication extends Application {
	
	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(this.getContext());
		router.attach("/provision", OpennsaRestHandlerForOvx.class);
		router.attach("/onosprovision", OpennsaRestHandlerForOnosVpls.class);
		router.attach("/topology", OVXTopologhyService.class);
		return router;
	}
	

}
