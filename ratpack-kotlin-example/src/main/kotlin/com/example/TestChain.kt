package com.example

import ratpack.kotlin.handling.KChainAction

class TestChain : KChainAction() {
  override fun execute() {
    get {
      render("testchain")
    }
  }
}
