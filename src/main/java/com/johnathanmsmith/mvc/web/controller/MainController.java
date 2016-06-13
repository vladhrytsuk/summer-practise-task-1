package com.johnathanmsmith.mvc.web.controller;

import com.johnathanmsmith.mvc.web.CarDto;
import com.johnathanmsmith.mvc.web.model.Car;
import com.johnathanmsmith.mvc.web.model.CarList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
     CarList ct;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = CarRestURIConstants.INDEX, method = RequestMethod.GET)
    public String MainIndex(){
        logger.debug("INDEX controller");
        return "index";
    }

//    @RequestMapping(value = CarRestURIConstants.TEST_ADD, method = RequestMethod.POST)
//    public ResponseEntity<CarList> add() {
//        CarList test = new CarList();
//
//        return new ResponseEntity<CarList>(test, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = CarRestURIConstants.TEST_ADD, method = RequestMethod.POST)
//    public ResponseEntity<CarList> edit(@RequestBody CarList test) {
//
//        return new ResponseEntity<CarList>(test, HttpStatus.OK);
//    }


    @RequestMapping(value = CarRestURIConstants.LIST, method = RequestMethod.GET)
    public @ResponseBody ModelAndView List(ModelMap model){
        model.put("ct",ct);
        logger.debug("LIST controller");
        return new ModelAndView("list");
    }

    @RequestMapping(value = CarRestURIConstants.ADD_CAR, method = RequestMethod.POST)
    public ModelAndView add(HttpServletRequest request){
        ModelAndView add = new ModelAndView("list");
        ct.addCar(new Car((UUID.randomUUID()), request.getParameter("mark"), request.getParameter("color"), Integer.valueOf(request.getParameter("vin")), Integer.valueOf(request.getParameter("miles"))));
        add.addObject("ct", ct);
        logger.debug("ADD controller");
        return add;
    }

    //@RequestMapping(value = CarRestURIConstants.DELETE_CAR, method = RequestMethod.POST)
    //public ModelAndView Delete(HttpServletRequest request) {
    //    ModelAndView delete = new ModelAndView("list");
     //   ct.deleteCar(Integer.valueOf(request.getParameter("deleteCar")) - 1);
    //    delete.addObject("ct", ct);
    //    logger.debug("DELETE controller");
    //    return delete;
   // }

//    @RequestMapping(value = CarRestURIConstants.EDIT_CAR, method = RequestMethod.POST)
//    public ModelAndView edit(HttpServletRequest request){
//        ModelAndView edit = new ModelAndView("list");
//        ct.editCar(Integer.valueOf(request.getParameter("idCar")) - 1,(new Car((UUID.randomUUID()), request.getParameter("mark"), request.getParameter("color"), Integer.valueOf(request.getParameter("vin")), Integer.valueOf(request.getParameter("miles")))));
//        edit.addObject("ct", ct);
//        logger.debug("EDIT controller");
//        return edit;
//    }

    @RequestMapping(value = CarRestURIConstants.DELETE_CAR, method = RequestMethod.POST)
    public String delete(@RequestParam(value = "deleteCar") UUID Id) {
        List<Car> car = ct.getCarListTable();
        for(int i = 0; i < car.size(); i++) {
            if (ct.getCarListTable().get(i).getId().equals(Id)){
                ct.deleteCar(i);
            }
        }
        return "redirect:/list";
    }

    @RequestMapping(value = CarRestURIConstants.EDIT_CAR, method = RequestMethod.POST)
    public ModelAndView edit(@RequestParam(value = "editCar") UUID carId ){
        ModelAndView model = new ModelAndView("edit");
        for(Car c : ct.getCarListTable()){
            if(c.getId().equals(carId)){
                model.addObject("car", c);
            }
        }
        return model;
    }

    @RequestMapping(value = CarRestURIConstants.EDIT_CAR_SAVE, method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request) {
        List<Car> car = ct.getCarListTable();
        ModelAndView model = new ModelAndView("edit");
        for(int i = 0;i < car.size(); i++) {
            if (ct.getCarListTable().get(i).getId().equals(UUID.fromString(request.getParameter("id")))){
                ct.getCarListTable().get(i).setMark(request.getParameter("mark"));
                ct.getCarListTable().get(i).setColor( request.getParameter("color"));
                ct.getCarListTable().get(i).setVin(Integer.valueOf(request.getParameter("vin")));
                ct.getCarListTable().get(i).setMiles(Integer.valueOf(request.getParameter("miles")));
            }
        }
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/list/addTest", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Car> addCar(@RequestBody Car car){
        ModelAndView add = new ModelAndView("list");
        ct.addCar(car);
        add.addObject("ct", ct);
        logger.debug("ADD controller");
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

}