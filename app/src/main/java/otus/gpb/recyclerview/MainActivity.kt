package otus.gpb.recyclerview

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import otus.gpb.recyclerview.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ChatAdapter
    private var currentPage = 1
    private val pageSize = 30
    var id = 1
    var previousTotalItemCount = 0
    private var isLoading = false
    private var chatList: MutableList<ChatItem> = mutableListOf()
    private val archiveList: MutableList<ChatItem> = mutableListOf()
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecyclerView()
        setFirstChats()
        generateRandomChats(pageSize)
        setScrolling()
    }


    private fun setScrolling(){
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                if (isLoading && totalItemCount > previousTotalItemCount) {
                    isLoading = false
                    previousTotalItemCount = totalItemCount
                }
                if (!isLoading && lastVisibleItem >= totalItemCount - 5) {
                    Log.d("MainActivity", currentPage.toString())
                    loadNextPage()
                }
            }
        })
    }
    private fun removeItem(position: Int){
        val itemToDelete = adapter.currentList[position]
        val newList = chatList.apply {
            remove(itemToDelete)
        }
        adapter.submitAntSortList(newList)
    }

    private fun setFirstChats(){
        chatList = generateRandomChats(currentPage).toMutableList()
        adapter.submitAntSortList(chatList)
    }

    private fun addInArchiveList(position: Int){
        val item = adapter.currentList[position]
        archiveList.add(item)
    }

    private fun loadNextPage() {
        isLoading = true
        val newChats = generateRandomChats(pageSize)
        chatList.addAll(newChats)
        adapter.submitAntSortList(chatList.toList())
        currentPage++
    }

    private fun generateRandomChats(size: Int): List<ChatItem>{
        val listRandomChats = mutableListOf<ChatItem>()
        for (i in 1..size) {
            listRandomChats.add(
                ChatItem(
                    id++,
                    getRandomCheckMark(),
                    true,
                    Random.nextBoolean(),
                    Random.nextBoolean(),
                    "12:10",
                    "Just design",
                    "Nicolay",
                    "I want pizza",
                    R.drawable.food_steak
                )
            )
        }
        return listRandomChats
    }

    private fun getRandomCheckMark(): String {
        val values = CheckMark.values().toList()
        val randomNumber = Random.nextInt(3)
        return values[randomNumber].name
    }

    private fun initRecyclerView() {
        adapter = ChatAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(CustomDecorator(this))
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when(direction){
                    ItemTouchHelper.LEFT -> {
                        addInArchiveList(position)
                        removeItem(position)
                        Log.d("MainActivity", archiveList.toString())
                    }
                    ItemTouchHelper.RIGHT -> {
                        removeItem(position)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.red
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.trash_can_outline)
                    .addSwipeRightLabel("Delete")
                    .setSwipeRightLabelColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.white
                        )
                    )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.blue
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.package_down)
                    .addSwipeLeftLabel("Archive")
                    .setSwipeLeftLabelColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.white
                        )
                    )
                    .create()
                    .decorate()
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
}