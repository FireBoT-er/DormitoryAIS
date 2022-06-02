package ru.cw.dormitoryais;

/**
 * Определяет действие, совершаемое в окне многофункциональной таблицы.
 * @see ru.cw.dormitoryais.Controllers.MultifunctionalTableWindowController
 */
public enum TableState {
    /**
     * Действие добавления чего-то к чему-то.
     */
    Add,
    /**
     * Действие обновления информации о чём-то.
     */
    Update,
    /**
     * Действие просмотра информации о чём-то.
     */
    ViewData,
    /**
     * Действие выбора чего-то.
     */
    Select
}
