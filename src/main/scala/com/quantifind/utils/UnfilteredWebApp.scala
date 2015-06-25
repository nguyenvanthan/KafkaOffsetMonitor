package com.quantifind.utils

import unfiltered.util.Port
import unfiltered.jetty.Server
import unfiltered.filter.Plan

import com.quantifind.sumac.{ArgMain, FieldArgs}
import com.quantifind.utils.UnfilteredWebApp.Arguments

/**
 * build up a little web app that serves static files from the resource directory
 * and other stuff from the provided plan
 * User: pierre
 * Date: 10/3/13
 */
trait UnfilteredWebApp[T <: Arguments] extends ArgMain[T] {

  def htmlRoot: String

  def setup(args: T): Plan

  def afterStart() {}

  def afterStop() {}

  override def main(parsed: T) {
    val root = getClass.getResource(htmlRoot)
    println("serving resources from: " + root)
    Server.http(parsed.port)
      .resources(root) //whatever is not matched by our filter will be served from the resources folder (html, css, ...)
      .plan(setup(parsed))
      .run(_ => afterStart(), _ => afterStop())
  }

}

object UnfilteredWebApp {

  trait Arguments extends FieldArgs {
    var port = Port.any
  }

}
