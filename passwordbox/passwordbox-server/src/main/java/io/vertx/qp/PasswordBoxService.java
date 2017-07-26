package io.vertx.qp;

import java.util.concurrent.ConcurrentMap;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import io.vertx.core.AbstractVerticle;

public class PasswordBoxService extends AbstractVerticle {

	public static void main(String[] args) {
		System.out.println("Main Start....");
	}

	@Override
	public void start() throws Exception {
		System.out.println("Vertx Start....");

		Config config = new Config();
		NetworkConfig network = config.getNetworkConfig();
		JoinConfig join = network.getJoin();
		join.getTcpIpConfig().addMember( "10.81.15.87" ).addMember( "10.60.215.40" ).setEnabled( true );
		HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
		ConcurrentMap<String, String> map = h.getMap("my-distributed-map");

		if (map.containsKey("mytest")) {
			System.out.println("do get:" + map.get("mytest"));
		} else {
			map.put("mytest", "somevalue");
			System.out.println("do set [mytest:somevalue]");
		}
	}

}
