package com.mycompany.tarea3;


import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.util.Date;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class PersonHandler extends BasicHandler {
    public PersonHandler(DataStorage storage) {
        super(storage);
    }

    public void handle(HttpExchange exchange) throws IOException {
        long timeStart=System.currentTimeMillis();
        String remoteAddress = exchange.getRemoteAddress().getHostString();
        System.out.println( 
            "[" + new Date() + "] Received GET " + exchange.getRequestURI() + " from client: " + remoteAddress);

        if ("GET".equals(exchange.getRequestMethod())) {
            String responseString = "";
            // Creamos la url en la que tenemos que meter los parametros adecuados como por
            // ejemplo el id en este caso
            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            String personId = params.get("id");
            try {
                if (personId != null) {
                    int id = Integer.parseInt(personId);
                    try {
                        Person person = storage.getPerson(id);
                        if (person != null) {
                            responseString += "{";
                            responseString += "\"header\": {";
                            responseString += "\"api_name\": \"API de Javier Garcia\",";
                            responseString += "\"api_version\": \"3.2.1\",";
                            responseString += "\"api_user\": \"usuario\",";
                            responseString += "\"api_endpoint\": \"api/get\",";
                            responseString += "\"http_request_method\": \""+exchange.getRequestMethod()+"\",";
                            responseString += "\"http_request_parameters\": \"X.X.X\",";
                            responseString += "\"http_response_status\": 200,";
                            responseString += "\"http_response_message\": \"OK\",";
                            responseString += "\"response_time\":"+(System.currentTimeMillis()-timeStart)+"";
                            responseString += "},";
                            responseString += "\"body\" :";
                            responseString += " {";
                            responseString += "\"id\": \"" + person.getId() + "\",";
                            responseString += "\"name\": \"" + person.getName() + "\",";
                            responseString += "\"about\": \"" + person.getAbout() + "\",";
                            responseString += "\"birthYear\": " + person.getBirthYear() + "";
                            responseString += " }";
                            responseString += "}";
                            exchange.sendResponseHeaders(200, responseString.getBytes().length);
                        } else {
                            responseString += "Not Found";
                            exchange.sendResponseHeaders(404, responseString.getBytes().length);
                        }

                    } catch (NumberFormatException a) {
                        responseString += "Bad Request";
                        exchange.sendResponseHeaders(400, responseString.getBytes().length);
                    }
                } else {
                    String personName = params.get("name");

                    if (personName != null) {
                        try {
                            Person person = storage.getPerson(personName);
                            
                            if (person != null) {
                                responseString += "{";
                                responseString += "\"header\": {";
                                responseString += "\"api_name\": \"API de Javier Garcia\",";
                                responseString += "\"api_version\": \"3.2.1\",";
                                responseString += "\"api_user\": \"usuario\",";
                                responseString += "\"api_endpoint\": \"api/get\",";
                                responseString += "\"http_request_method\": \""+exchange.getRequestMethod()+"\",";
                                responseString += "\"http_request_parameters\": \"X.X.X\",";
                                responseString += "\"http_response_status\": 200 ,";
                                responseString += "\"http_response_message\": \"OK\",";
                                responseString += "\"response_time\":"+(System.currentTimeMillis()-timeStart)+"";
                                responseString += "},";
                                responseString += "\"body\" :";
                                responseString += "  {";
                                responseString += "\"id\": \"" + person.getId() + "\",";
                                responseString += "\"name\": \"" + person.getName() + "\",";
                                responseString += "\"about\": \"" + person.getAbout() + "\",";
                                responseString += "\"birthYear\": \"" + person.getBirthYear() + "\"";
                                responseString += "  }";
                                responseString += "}";
                                exchange.sendResponseHeaders(200, responseString.getBytes().length);
                            } else {
                                responseString += "Not Found}";
                                exchange.sendResponseHeaders(404, responseString.getBytes().length);
                            }

                        } catch (Exception a) {
                            responseString += "Bad Request}";
                            exchange.sendResponseHeaders(400, responseString.getBytes().length);
                        }
                    }
                }
            } catch (Exception e) {
                responseString = "Bad Request}";
                exchange.sendResponseHeaders(400, responseString.getBytes().length);
            }

            /*
             * In the real world this part should be implemented this way:
             * JSONObject jsonObject = new JSONObject();
             * jsonObject.put("name", person.getName());
             * jsonObject.put("about", person.getAbout());
             * jsonObject.put("birthYear", person.getBirthYear());
             * responseString = jsonObject.toString();
             */

            OutputStream output = exchange.getResponseBody();
            output.write(responseString.getBytes());
            output.flush();
        } else if ("POST".equals(exchange.getRequestMethod())) {
            String responseString = "";

            String nombrePersona = "", about = "";
            String fechaNac = "";
            String idPersona = "";

            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            idPersona = params.get("id");
            nombrePersona = params.get("name");
            about = params.get("about");
            fechaNac = params.get("birthYear");

            if (nombrePersona != null && idPersona != null) {

                Person nuevaPersona = new Person(Integer.parseInt(idPersona), nombrePersona, about,
                        Integer.parseInt(fechaNac));
                try {
                    storage.putPerson(nuevaPersona);
                                responseString += "{";
                                responseString += "\"header\": {";
                                responseString += "\"api_name\": \"API de Javier Garcia\",";
                                responseString += "\"api_version\": \"3.2.1\",";
                                responseString += "\"api_user\": \"usuario\",";
                                responseString += "\"api_endpoint\": \"api/get\",";
                                responseString += "\"http_request_method\": \""+exchange.getRequestMethod()+"\",";
                                responseString += "\"http_request_parameters\": \"X.X.X\",";
                                responseString += "\"http_response_status\": 200 ,";
                                responseString += "\"http_response_message\": \"OK\",";
                                responseString += "\"response_time\":"+(System.currentTimeMillis()-timeStart)+"";
                                responseString += "},";
                                responseString += "\"body\" :";
                                responseString += "  {";
                    responseString += "\"id\": \"" + nuevaPersona.getId() + "\",";
                    responseString += "\"name\": \"" + nuevaPersona.getName() + "\",";
                    responseString += "\"about\": \"" + nuevaPersona.getAbout() + "\",";
                    responseString += "\"birthYear\": " + nuevaPersona.getBirthYear() + "";
                    responseString += " }";
                    responseString += "}";
                    exchange.sendResponseHeaders(200, responseString.getBytes().length);
                } catch (Exception b) {
                    responseString = "Bad Request}";
                    exchange.sendResponseHeaders(400, responseString.getBytes().length);
                }
            } else {
                responseString += "Not Found}";
                exchange.sendResponseHeaders(404, responseString.getBytes().length);
            }
            OutputStream output = exchange.getResponseBody();
            output.write(responseString.getBytes());
            output.flush();

        } else if ("PUT".equals(exchange.getRequestMethod())) {
            String responseString = "";

            String idPersona = "", nuevoNombrePersona = "", nuevoAbout = "";
            String nuevoFechaNac = "";

            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            idPersona = params.get("id");
            nuevoNombrePersona = params.get("newName");
            nuevoAbout = params.get("newAbout");
            nuevoFechaNac = params.get("newBirthYear");
            if (idPersona != null) {
                Person personaNueva = new Person(Integer.parseInt(idPersona), nuevoNombrePersona, nuevoAbout,
                        Integer.parseInt(nuevoFechaNac));
                try {

                    storage.changePerson(Integer.parseInt(idPersona), personaNueva);
                    responseString += "{";
                            responseString += "\"header\": {";
                            responseString += "\"api_name\": \"API de Javier Garcia\",";
                            responseString += "\"api_version\": \"3.2.1\",";
                            responseString += "\"api_user\": \"usuario\",";
                            responseString += "\"api_endpoint\": \"api/get\",";
                            responseString += "\"http_request_method\": \""+exchange.getRequestMethod()+"\",";
                            responseString += "\"http_request_parameters\": \"X.X.X\",";
                            responseString += "\"http_response_status\": 200 ,";
                            responseString += "\"http_response_message\": \"OK\",";
                            responseString += "\"response_time\":"+(System.currentTimeMillis()-timeStart)+"";
                            responseString += "},";
                            responseString += "\"body\" :";
                            responseString += "  {";
                    responseString += "\"id\": \"" + personaNueva.getId() + "\",";
                    responseString += "\"name\": \"" + personaNueva.getName() + "\",";
                    responseString += "\"about\": \"" + personaNueva.getAbout() + "\",";
                    responseString += "\"birthYear\": " + personaNueva.getBirthYear() + "";
                    responseString += " }";
                    responseString += "}";
                    exchange.sendResponseHeaders(200, responseString.getBytes().length);
                } catch (Exception d) {
                    responseString = "Bad Request}";
                    exchange.sendResponseHeaders(400, responseString.getBytes().length);
                }
            } else {
                responseString += "Not Found}";
                exchange.sendResponseHeaders(404, responseString.getBytes().length);
            }
            OutputStream output = exchange.getResponseBody();
            output.write(responseString.getBytes());
            output.flush();

        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            String responseString = "";

            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            String personId = params.get("id");
            if (personId != null) {
                int id = Integer.parseInt(personId);
                Person person = storage.getPerson(id);
                if (person != null) {
                    try {

                        storage.deletePerson(id);
                        responseString += "{";
                                responseString += "\"header\": {";
                                responseString += "\"api_name\": \"API de Javier Garcia\",";
                                responseString += "\"api_version\": \"3.2.1\",";
                                responseString += "\"api_user\": \"usuario\",";
                                responseString += "\"api_endpoint\": \"api/get\",";
                                responseString += "\"http_request_method\": \""+exchange.getRequestMethod()+"\",";
                                responseString += "\"http_request_parameters\": \"X.X.X\",";
                                responseString += "\"http_response_status\": 200 ,";
                                responseString += "\"http_response_message\": \"OK\",";
                                responseString += "\"response_time\":"+(System.currentTimeMillis()-timeStart)+"";
                                responseString += "},";
                                responseString += "\"body\" :";
                                responseString += "  {";
                        responseString += "\"id\": \"" + person.getId() + "\",";
                        responseString += "\"name\": \"" + person.getName() + "\",";
                        responseString += "\"about\": \"" + person.getAbout() + "\",";
                        responseString += "\"birthYear\": " + person.getBirthYear() + "";
                        responseString += " }";
                        responseString += "}";
                        exchange.sendResponseHeaders(200, responseString.getBytes().length);

                    } catch (Exception c) {
                        responseString = "Bad Request}";
                        exchange.sendResponseHeaders(400, responseString.getBytes().length);
                    }
                }else{
                    responseString += "Bad Request}";
                    exchange.sendResponseHeaders(400, responseString.getBytes().length);
                }
            } else {
                responseString += "Not Found}";
                exchange.sendResponseHeaders(404, responseString.getBytes().length);
            }
            OutputStream output = exchange.getResponseBody();
            output.write(responseString.getBytes());
            output.flush();
        } else {
            exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
        }

        exchange.close();
    }
}