package ru.kamanin.nstu.graduate.thesis.feature.exam.ticket.navigation

import android.os.Bundle
import ru.kamanin.nstu.graduate.thesis.component.navigation.NavCommand

interface TicketNavigationProvider {

	fun toChat(): NavCommand

	fun toTask(args: Bundle): NavCommand
}