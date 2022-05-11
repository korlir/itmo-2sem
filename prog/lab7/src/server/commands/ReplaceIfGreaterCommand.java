package server.commands;

import connect_utils.CommandInfo;
import data_classes.City;
import exceptions.IncorrectArgumentException;

import java.io.IOException;
import java.sql.SQLException;

public class ReplaceIfGreaterCommand extends Command {

    ReplaceIfGreaterCommand() {
        super("replace_if_greater", "id {element}", "заменяет значение по id, если новое значение больше старого",
                CommandInfo.SendInfo.CITY_UPDATE,
                new CommandInfo.ArgumentInfo[]{CommandInfo.ArgumentInfo.ID}, false);
    }

    /**
     * replace all fields in element with id from args that be lower than new fields
     * <p>Modification time can be changed</p>
     *
     * @param commandController that uses for program
     * @param args              id
     * @throws IncorrectArgumentException if id is incorrect
     */
    @Override
    public String execute(CommandController commandController, String[] args)
            throws IncorrectArgumentException, IOException, ClassNotFoundException {
        long id = Long.parseLong(args[1]);
        if (commandController.getDataController().checkUniqueID(id))
            throw new IncorrectArgumentException("элемента с данным id не существует");
        try {
            if (!commandController.getDataController().getDataBaseController().isOwner(args[0], id))
                throw new IncorrectArgumentException("вы не владеете этим элементом. Вы не можете изменять его.");

            commandController.getConnectionController().getRequestController().sendOK();
            City city = commandController.getConnectionController().getRequestController().receiveCity();
            city.setId(id);
            deleteNullValues(commandController.getDataController().getMap().get(id), city);
            replaceCity(commandController.getDataController().getMap().get(id), city);
            commandController.getDataController().updateCity(city);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IncorrectArgumentException("не удалось обновить элемент в базе данных");
        }
        commandController.getDataController().updateModificationTime();
        return "Значение элемента с id " + id + " было обновлено.\n";

    }

    /**
     * Change newCity's fields
     * <p>newCity's fields will change after this method</p>
     *
     * @param oldCity from collection
     * @param newCity from console entering
     */
    private void replaceCity(final City oldCity, City newCity) {
        if (oldCity.getName().compareTo(newCity.getName()) < 0) {
            oldCity.setName(newCity.getName());
        }
        if (oldCity.getCoordinates().getX() < newCity.getCoordinates().getX()) {
            oldCity.getCoordinates().setX(newCity.getCoordinates().getX());
        }
        if (oldCity.getCoordinates().getY() < newCity.getCoordinates().getY()) {
            oldCity.getCoordinates().setY(newCity.getCoordinates().getY());
        }
        if (oldCity.getEstablishmentDate().isBefore(newCity.getEstablishmentDate())) {
            oldCity.setEstablishmentDate(newCity.getEstablishmentDate());
        }
        if (oldCity.getArea() < newCity.getArea()) {
            oldCity.setArea(oldCity.getArea());
        }
        if (oldCity.getPopulation() < newCity.getPopulation()) {
            oldCity.setPopulation(newCity.getPopulation());
        }
        if (oldCity.getMetersAboveSeaLevel() < newCity.getMetersAboveSeaLevel()) {
            oldCity.setMetersAboveSeaLevel(newCity.getMetersAboveSeaLevel());
        }
        if (oldCity.getClimate() == null)
            oldCity.setClimate(newCity.getClimate());
        else if (newCity.getClimate() == null) ;
        else if (oldCity.getClimate().ordinal() < newCity.getClimate().ordinal()) {
            oldCity.setClimate(newCity.getClimate());
        }
        if (oldCity.getGovernment() == null)
            oldCity.setGovernment(newCity.getGovernment());
        else if (newCity.getGovernment() == null) ;
        if (oldCity.getGovernment().ordinal() < newCity.getGovernment().ordinal()) {
            oldCity.setGovernment(newCity.getGovernment());
        }
        if (oldCity.getGovernor().getAge() < newCity.getGovernor().getAge()) {
            oldCity.getGovernor().setAge(newCity.getGovernor().getAge());
        }
        if (oldCity.getGovernor().getBirthday().isBefore(newCity.getGovernor().getBirthday())) {
            oldCity.getGovernor().setBirthday(newCity.getGovernor().getBirthday());
        }
        newCity = oldCity;
    }
}
