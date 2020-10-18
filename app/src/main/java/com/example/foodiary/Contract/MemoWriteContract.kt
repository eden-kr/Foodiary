package com.example.foodiary.Contract

class MemoWriteContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun limitLine()
        fun setOutLine()
        fun setMemoBackground()
        fun setLottie()
        fun setDate(date : String)
    }
    interface Presenter{
        fun attachView(view : View)
        fun detachView()
    }
}