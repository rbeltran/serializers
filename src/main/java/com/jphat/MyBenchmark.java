/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jphat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@State(Scope.Thread)
public class MyBenchmark {

	private byte[] serializedBytes;
	private byte[] gzippedJsonBytes;
	private byte[] jsonSerializedBytes;
	
	private ObjectMapper objectMapper;
	
	private List<User> users;
	private List<User> users2;
	private List<User> users3;
	private List<User> users4;
	
	
	@Setup(Level.Trial)
	public void init() {
		users = buildUsers();
		objectMapper = new ObjectMapper();
	}

	@TearDown(Level.Trial)
	public void check() {
		//Sanity check at the end
		assert users.equals( users2 );
		assert users.equals( users3 );
		assert users.equals( users4 );
	}
	
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)    
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
    	serializeUsers(users);
    	users2 = deserializeUsers();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)    
    public void testJsonGzipped() throws IOException {
    	gzipJsonUsers(users);
    	users3 = gunzipJsonUsers();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)    
    public void testJsonSerialization() throws IOException {
    	jsonSerializeUsers(users);
    	users4 = jsonDeserializeUsers();
    }
    
    private void jsonSerializeUsers( List<User> users ) throws JsonProcessingException {
    	jsonSerializedBytes = objectMapper.writeValueAsBytes(users);
    }
    
	private List<User> jsonDeserializeUsers() throws JsonParseException, JsonMappingException, IOException {
    	return objectMapper.readValue(jsonSerializedBytes, new TypeReference<List<User>>(){});
    }
    
	private void gzipJsonUsers( List<User> users ) throws IOException {

		byte[] jsonBytes = objectMapper.writeValueAsBytes(users);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream);
		gzipStream.write(jsonBytes);
		gzipStream.close();
		gzippedJsonBytes = byteStream.toByteArray();
		byteStream.close();

	}
	
	private List<User> gunzipJsonUsers() throws IOException {
		
		ByteArrayInputStream byteStream = new ByteArrayInputStream(gzippedJsonBytes);
		GZIPInputStream inStream = new GZIPInputStream( byteStream );
		
		//
		// take the gzipped stream and read off the bytes into a ByteArrayOutputStream
		// to be read into a JSON string object
		//
		byte[] buffer = new byte[1024];
		int len;
		ByteArrayOutputStream ungzippedBytes = new ByteArrayOutputStream();
        while((len = inStream.read(buffer)) != -1){
        	ungzippedBytes.write(buffer, 0, len);
        }

		inStream.close();
		ungzippedBytes.close();
		return objectMapper.readValue(ungzippedBytes.toByteArray(), new TypeReference<List<User>>(){});
	}

	private void serializeUsers( List<User> users ) throws IOException {
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream outStream = new ObjectOutputStream( byteStream );
		outStream.writeObject( users );
		outStream.flush();
		serializedBytes = byteStream.toByteArray();
		outStream.close();
		
	}
	
	@SuppressWarnings("unchecked")
	private List<User> deserializeUsers() throws IOException, ClassNotFoundException {

		ByteArrayInputStream byteStream = new ByteArrayInputStream(serializedBytes);
		ObjectInputStream inStream = new ObjectInputStream( byteStream );
		List<User> users = (List<User>) inStream.readObject();
		inStream.close();
		
		
		return users;
		
	}
	
	private List<User> buildUsers() {
		User user;
		Address home;
		Address work;
		HashMap<String, String> properties;

		List<User> users = new ArrayList<User>();
		
		Random ran = new Random();
		
		for( int i=0; i < 1000; i++ ) {
			
			home = new Address( Double.toString(ran.nextDouble()),
					Double.toString(ran.nextDouble()),
					Double.toString(ran.nextDouble()),
					Double.toString(ran.nextDouble()));
			work = new Address( Double.toString(ran.nextDouble()),
					Double.toString(ran.nextDouble()),
					Double.toString(ran.nextDouble()),
					Double.toString(ran.nextDouble()));
			
			properties = new HashMap<String,String>();
			properties.put("car", Double.toString(ran.nextDouble()));
			properties.put("age", Double.toString(ran.nextDouble()));
			properties.put("likes", Double.toString(ran.nextDouble()));
			properties.put("dislikes", Double.toString(ran.nextDouble()));
			properties.put("favorite color", Double.toString(ran.nextDouble()));
			
			user = new User( home,
					work,
					Double.toString(ran.nextDouble()),
					Double.toString(ran.nextDouble()),
					Double.toString(ran.nextDouble()),
					properties
					);
			users.add(user);
		}
		
		return users;
	}


}
