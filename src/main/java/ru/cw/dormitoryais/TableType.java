package ru.cw.dormitoryais;

/**
 * Определяет данные, добавляемые в окно многофункциональной таблицы.
 * @see ru.cw.dormitoryais.Controllers.MultifunctionalTableWindowController
 */
public enum TableType {
    /**
     * Данные об инвентаре.
     */
    Inventory,
    /**
     * Данные о нарушениях.
     */
    Violations,
    /**
     * Данные о посетителях.
     */
    Visitors,
    /**
     * Данные о проживающих в комнате студентах.
     */
    Residents,
    /**
     * Данные об обладателях инвентаря.
     */
    Holders,
    /**
     * Данные об уборках.
     */
    Cleanings,
    /**
     * Данные об убиравшихся сотрудниках.
     */
    Cleaners,
    /**
     * Данные о посещённых студентах.
     */
    Visited,
    /**
     * Данные о комнатах.
     */
    Rooms
}
